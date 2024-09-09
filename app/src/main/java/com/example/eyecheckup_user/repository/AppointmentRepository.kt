package com.example.eyecheckup_user.repository

import com.example.eyecheckup_user.data.DataOrException
import com.example.eyecheckup_user.model.M_Appointment
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AppointmentRepository @Inject constructor(private val queryTrack : Query) {

    suspend fun getAllAppointmentFromDatabase() : DataOrException<List<M_Appointment>, Boolean, Exception> {
        val dataOrException = DataOrException<List<M_Appointment>,Boolean,Exception>()

        try {
            dataOrException.loading = true
            dataOrException.data = queryTrack.get().await().documents.map { documentSnapshot ->
                documentSnapshot.toObject(M_Appointment::class.java)!!
            }
            if (!dataOrException.data.isNullOrEmpty())  dataOrException.loading = false

        }catch (exception : FirebaseFirestoreException){
            dataOrException.e = exception
        }
        return dataOrException
    }
}