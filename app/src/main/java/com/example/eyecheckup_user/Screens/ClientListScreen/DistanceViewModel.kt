package com.example.eyecheckup_user.Screens.ClientListScreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eyecheckup_user.DistanceRequest
import com.example.eyecheckup_user.Location
import com.example.eyecheckup_user.model.DistanceResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.IOException

class DistanceViewModel : ViewModel() {

    private val _response = MutableLiveData<DistanceResponse?>()
    val response: LiveData<DistanceResponse?> get() = _response

    fun fetchDistance(sources: List<Location>, targets: List<Location>) {
        Log.d("DistanceViewModel","CalledViewModel")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val distanceRequest = DistanceRequest(
                    mode = "drive",
                    sources = sources,
                    targets = targets
                )

                val jsonPayload = Gson().toJson(distanceRequest)

                val jsonResponse = makePostRequest(jsonPayload)
                val distanceResponse = Gson().fromJson(jsonResponse, DistanceResponse::class.java)
                Log.d("DistanceViewModel",distanceResponse.toString())
                _response.postValue(distanceResponse)

            } catch (e: Exception) {
                // Handle exceptions
                e.printStackTrace()
            }
        }
    }

    private fun makePostRequest(jsonPayload: String): String {
        val client = OkHttpClient()
        val mediaType = "application/json".toMediaType()
        val body = RequestBody.create(mediaType, jsonPayload)

        val request = Request.Builder()
            .url("https://api.geoapify.com/v1/routematrix?apiKey=3246256222d44cf0a5ee12712d0ce69c")
            .post(body)
            .addHeader("Content-Type", "application/json")
            .build()

        client.newCall(request).execute().use { response ->
            return if (response.isSuccessful) {
                response.body?.string() ?: ""
            } else {
                throw IOException("Unexpected code $response")
            }
        }
    }
}