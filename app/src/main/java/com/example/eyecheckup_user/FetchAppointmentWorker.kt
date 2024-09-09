package com.example.eyecheckup_user

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.eyecheckup_user.model.M_Appointment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class FetchAppointmentsWorker(
    appContext: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(appContext, workerParams) {

    private val sharedPreferences: SharedPreferences =
        appContext.getSharedPreferences("notified_appointments", Context.MODE_PRIVATE)

    val activity = appContext as? Activity
    override suspend fun doWork(): Result {
        return try {
            val db: FirebaseFirestore = Firebase.firestore
            val result = db.collection("appointments").get().await()
            val appointments = result.map { document ->
                val appointment = document.toObject(M_Appointment::class.java)
                appointment.docid = document.id
                appointment
            }
            appointments.forEach { appointment ->
                if (appointment.BookingState == "Confirmed" && !isNotified(appointment.docid)) {
                    sendNotification(
                        applicationContext,
                        "Appointment Fixed",
                        "Appointment with ${appointment.doctorName} at ${appointment.Priority}"
                    )
                    markAsNotified(appointment.docid)
                }
            }
            // Do something with the appointments, like saving to local storage
            Log.d("FetchAppointmentsWorker", "Fetched ${appointments} appointments")

            Result.success()
        } catch (e: Exception) {
            Log.e("FetchAppointmentsWorker", "Error fetching appointments", e)
            Result.failure()
        }
    }

    private fun isNotified(docid: String?): Boolean {
        return docid != null && sharedPreferences.getBoolean(docid, false)
    }

    private fun markAsNotified(docid: String?) {
        if (docid != null) {
            sharedPreferences.edit().putBoolean(docid, true).apply()
        }
    }
}


