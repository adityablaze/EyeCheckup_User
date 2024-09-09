package com.example.eyecheckup_user


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.eyecheckup_user.Screens.AppointmentScreen.AppointmentViewmodel
import com.example.eyecheckup_user.Screens.ClientListScreen.DistanceViewModel
import com.example.eyecheckup_user.Screens.LocationScreen.LocationHelper
import com.example.eyecheckup_user.Screens.LoginScreen.PhoneAuthHelper
import com.example.eyecheckup_user.model.M_Appointment
import com.example.eyecheckup_user.naviation.CheckUpNaviation
import com.example.eyecheckup_user.ui.theme.EyeCheckup_UserTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var locationHelper: LocationHelper
    private lateinit var phoneAuthHelper: PhoneAuthHelper

    val requestNotificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Permission granted, you can proceed with sending notification
            } else {
                // Permission denied, handle the case
            }
        }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        locationHelper = LocationHelper(this)
        phoneAuthHelper = PhoneAuthHelper(this)

        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                locationHelper.checkGpsAndFetchLocation()
            } else {
                Toast.makeText(this, "Location permission is required", Toast.LENGTH_SHORT).show()
            }
        }

        val enableGpsLauncher = registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                locationHelper.checkGpsAndFetchLocation()
            } else {
                Toast.makeText(this, "GPS is required for this feature", Toast.LENGTH_SHORT).show()
            }
        }

        

        setContent {
            val locationState = locationHelper.locationState
            val isLocationPermissionGranted = locationHelper.isLocationPermissionGranted
            val showPermissionDialog = locationHelper.showPermissionDialog
            val appointmentViewmodel : AppointmentViewmodel = hiltViewModel()

            EyeCheckup_UserTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val VisionScore = remember { mutableStateListOf<Int?>() }

                    checkAndRequestNotificationPermission()

                    CheckUpNaviation(
                        phoneAuthHelper,
                        locationHelper,
                        VisionScore.toList(),
                        locationRequest = {locationHelper.initialize(requestPermissionLauncher, enableGpsLauncher)},
                        onClick_Vision = {score  -> VisionScore.add(score) },
                        onClear_Score = {
                            VisionScore.toMutableList().forEach { score ->
                                VisionScore.remove(score)
                            }
                        }
                    )
                }
            }

        }

        val workRequest = PeriodicWorkRequestBuilder<FetchAppointmentsWorker>(15, TimeUnit.MINUTES)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "FetchAppointmentsWorker",
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )
    }

    private fun checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13 and above
            when {
                ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED -> {
                   //don't do anything
                }
                else -> {
                    // Directly request for the permission
                    requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            // If the Android version is lower than 13, no need to request this permission
        }
    }
}


data class Location(val location: List<Double>)


data class DistanceRequest(
    val mode: String,
    val sources: List<Location>,
    val targets: List<Location>
)

