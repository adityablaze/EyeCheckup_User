package com.example.eyecheckup_user.Screens.ClientListScreen

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eyecheckup_user.data.DataOrException
import com.example.eyecheckup_user.model.M_Client
import com.example.eyecheckup_user.repository.FireRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientViewModel @Inject constructor(private val  userRepository: FireRepository) : ViewModel() {

    val clients : MutableState<DataOrException<List<M_Client>, Boolean, Exception>>
            = mutableStateOf(DataOrException(listOf(),true,Exception("")))

    init {
        getAllUsersFromDatabase()
    }

    private fun getAllUsersFromDatabase() {
        viewModelScope.launch {
            clients.value.loading = true
            clients.value = userRepository.getAllUsersFromDatabase()
            if (!clients.value.data.isNullOrEmpty()) clients.value.loading = false
        }
        Log.d("FireBase","getalAll : ${clients.value.data?.toList().toString()}")
    }
}