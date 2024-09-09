package com.example.eyecheckup_user.Screens.LocationScreen

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest

class LocationHelper(private val activity: Activity) {

    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)
    val locationState = mutableStateOf<Location?>(null)
    val isLocationPermissionGranted = mutableStateOf(false)
    val showPermissionDialog = mutableStateOf(false)

    lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    lateinit var enableGpsLauncher: ActivityResultLauncher<IntentSenderRequest>

    fun initialize(
        requestPermissionLauncher: ActivityResultLauncher<String>,
        enableGpsLauncher: ActivityResultLauncher<IntentSenderRequest>
    ) {
        this.requestPermissionLauncher = requestPermissionLauncher
        this.enableGpsLauncher = enableGpsLauncher

        // Check if location permission is already granted
        when {
            ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED -> {
                isLocationPermissionGranted.value = true
                checkGpsAndFetchLocation()
            }
            else -> {
                // Show permission dialog
                showPermissionDialog.value = true
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun fetchLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                locationState.value = location
            }
    }

    fun checkGpsAndFetchLocation() {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

        val settingsClient = LocationServices.getSettingsClient(activity)
        val task = settingsClient.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            fetchLocation()
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    val intentSenderRequest = IntentSenderRequest.Builder(exception.resolution).build()
                    enableGpsLauncher.launch(intentSenderRequest)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Error launching GPS resolution
                    Toast.makeText(activity, "Error resolving GPS", Toast.LENGTH_SHORT).show()
                }
            } else {
                // GPS settings not resolvable
                Toast.makeText(activity, "GPS settings are not resolvable", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun requestPermission() {
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }
}