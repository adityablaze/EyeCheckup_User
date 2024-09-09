package com.example.eyecheckup_user.Screens.AppointmentScreen

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eyecheckup_user.data.DataOrException
import com.example.eyecheckup_user.model.M_Appointment
import com.example.eyecheckup_user.model.M_Client
import com.example.eyecheckup_user.repository.AppointmentRepository
import com.example.eyecheckup_user.repository.FireRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppointmentViewmodel @Inject constructor(private val  appointmentRepository: AppointmentRepository) : ViewModel() {

    val Appointment : MutableState<DataOrException<List<M_Appointment>, Boolean, Exception>>
            = mutableStateOf(DataOrException(listOf(),true,Exception("")))

    init {
        getAllAppointmentFromDatabase()
    }

    fun getAllAppointmentFromDatabase() {
        viewModelScope.launch {
            Appointment.value.loading = true
            Appointment.value = appointmentRepository.getAllAppointmentFromDatabase()
            if (!Appointment.value.data.isNullOrEmpty()) {
                Appointment.value.loading = false
                Log.d("Appointments", "getalAll : ${Appointment.value.data.toString()}")
            }
        }
    }
}