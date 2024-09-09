package com.example.eyecheckup_user.Components

import android.util.Log
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eyecheckup_user.model.M_Client
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    imageVector: ImageVector? = null,
    title : String,
    backEnabled : Boolean,
    onClick: () -> Unit

){
    TopAppBar(
        actions = {
            if (backEnabled) {
                IconButton(
                    onClick = {
                        onClick.invoke()
                    }
                ) {
                    if (imageVector != null) {
                        Icon(imageVector = imageVector, contentDescription = "" , tint = Color.White)
                    }
                }
            }
        },
        navigationIcon = {
            if (!backEnabled) {
                IconButton(
                    onClick = {
                        onClick.invoke()
                    }
                ) {
                    if (imageVector != null) {
                        Icon(imageVector = imageVector, contentDescription = "" , tint = Color.White)
                    }
                }
            }
        },
        title = { Text(text = title , color = Color.White) } ,
        colors = TopAppBarDefaults
            .centerAlignedTopAppBarColors(containerColor = Color(0xFF1F4037)),
    )
}

@Composable
fun inchToSp(Inch: Double) : TextUnit {
    val context = LocalContext.current
    val displayMetrics = context.resources.displayMetrics
    val screenWidthPx = displayMetrics.widthPixels
    val screenWidthInches = screenWidthPx / displayMetrics.xdpi
    val pixelWidthSize = screenWidthInches / screenWidthPx
    val density = LocalDensity.current.density
    val scaledDensity = LocalDensity.current.fontScale * density
    val InchToSp =   (Inch / (scaledDensity * pixelWidthSize) ).sp //1 inch
    val InchToDp =   (Inch / (density * pixelWidthSize) ) //1 inch

    return InchToSp

}

@Composable
fun inchToDp(Inch : Float) : Dp {
    val context = LocalContext.current
    val displayMetrics = context.resources.displayMetrics
    val screenWidthPx = displayMetrics.widthPixels
    val screenWidthInches = screenWidthPx / displayMetrics.xdpi
    val pixelWidthSize = screenWidthInches / screenWidthPx
    val density = LocalDensity.current.density
    val scaledDensity = LocalDensity.current.fontScale * density
    val InchToSp =   (Inch / (scaledDensity * pixelWidthSize) ).sp //1 inch
    val InchToDp =   (Inch / (density * pixelWidthSize) ).dp //1 inch

    return InchToDp

}

@Composable
fun ShowDialog(
    title : String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
){
    AlertDialog(
        onDismissRequest = { onDismiss.invoke() },
        title = { Text(text = title)},
        confirmButton = {
                        Button(
                            onClick = { onConfirm.invoke() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1F4037) , contentColor = Color.White)
                            ) {
                            Text(text = "exit")
                        }
        },
        dismissButton = {
            Button(
                onClick = { onDismiss.invoke() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1F4037) , contentColor = Color.White),
                ) {
                     Text(text = "cancel")
            }
        }
    )
}


fun saveToFirebase(
    track: M_Client,
    onSuccess: () -> Unit = {}
) {
    val db = FirebaseFirestore.getInstance()
    val dbCollection = db.collection("clients")
    val mClient = M_Client(
        uid = FirebaseAuth.getInstance().currentUser?.uid.toString(),
        clinicName = track.clinicName,
        doctorName = track.doctorName,
        address = track.address,
        latitude = track.latitude,
        longitude = track.longitude,
        contactNumber = track.contactNumber,
        imageUrl = track.imageUrl
    )
    if (!mClient.toString().isNullOrEmpty()){
        dbCollection.add(mClient)
            .addOnSuccessListener { documentRef ->
                val docId = documentRef.id
                dbCollection.document(docId)
                    .update(hashMapOf( "docid" to docId ) as Map<String,Any>)
                    .addOnCompleteListener{task ->
                        if (task.isSuccessful){
                            Log.d("FirebaseSave" , "saveToFirebase : ${task}")
                            onSuccess.invoke()
                        }
                    }
                    .addOnFailureListener{
                        Log.w("Error","Savetofirebase : " , it)
                    }
            }
    }
}

