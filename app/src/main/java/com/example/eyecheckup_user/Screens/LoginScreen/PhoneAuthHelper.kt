package com.example.eyecheckup_user.Screens.LoginScreen

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.TimeUnit

class PhoneAuthHelper(private val activity: Activity) {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private  var verificationId: String = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    private val verificationCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            signInWithPhoneAuthCredential(credential, onfailure = {} , success = {})
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Log.e("PhoneAuth", "Verification failed: ${e.message}")
            Toast.makeText(activity, "Verification failed: ${e.message}", Toast.LENGTH_LONG).show()
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            this@PhoneAuthHelper.verificationId = verificationId
            resendToken = token
            Toast.makeText(activity, "Verification code sent", Toast.LENGTH_SHORT).show()
        }
    }

    fun startPhoneNumberVerification(phoneNumber: String) {
        verificationId = ""
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(verificationCallbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
    fun codeStatus(
        status: (String) -> Unit
    ){
        status(verificationId)
    }
    fun verifyPhoneNumberWithCode(
        code: String,
        success: () -> Unit,
        onfailure : (String) -> Unit
    ) {
        if (verificationId.isNotEmpty()) {
            val credential = PhoneAuthProvider.getCredential(verificationId, code)
            signInWithPhoneAuthCredential(
                credential,
                onfailure = { string -> onfailure(string) },
                success = { success.invoke() })
        }
    }

    private fun signInWithPhoneAuthCredential(
        credential: PhoneAuthCredential,
        success: () -> Unit,
        onfailure : (String) -> Unit
    ) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    Log.d("PhoneAuth", "signInWithCredential:success")
                    val user = task.result?.user
                    // Proceed with Firestore operations if needed
                    user?.let {
                        val userDoc = firestore.collection("users").document(it.uid)
                        userDoc.get()
                            .addOnSuccessListener { document ->
                                if (document.exists()) {
                                    success.invoke()
                                    Log.d("Firestore", "User document exists.")
                                    // User document exists, proceed accordingly
                                } else {
                                    success.invoke()
                                    Log.d("Firestore", "User document does not exist. Creating a new document.")
                                    val newUser = hashMapOf(
                                        "phoneNumber" to it.phoneNumber,
                                        "uid" to it.uid
                                    )
                                    userDoc.set(newUser)
                                        .addOnSuccessListener {
                                            Log.d("Firestore", "User document created.")
                                        }
                                        .addOnFailureListener { e ->
                                            Log.e("Firestore", "Error creating user document: ", e)
                                        }
                                }
                            }
                            .addOnFailureListener { e ->
                                onfailure("Error Verifying")
                                Log.e("Firestore", "Error fetching user document: ", e)
                            }
                    }
                } else {
                    Log.w("PhoneAuth", "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        onfailure("Invalid code entered")
                        Toast.makeText(activity, "Invalid code entered", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    fun resendVerificationCode(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(verificationCallbacks)
            .setForceResendingToken(resendToken)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}