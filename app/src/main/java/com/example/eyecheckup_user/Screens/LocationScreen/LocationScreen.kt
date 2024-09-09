package com.example.eyecheckup_user.Screens.LocationScreen

import android.location.Location
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.eyecheckup_user.naviation.CheckupScreens
import kotlinx.coroutines.delay

@Composable
fun LocationScreen(
    locationHelper: LocationHelper,
    navController: NavController,
    locationRequest: () -> Unit,
) {
    LaunchedEffect(Unit){
        locationRequest.invoke()
    }

    var locationState = locationHelper.locationState
    val isLocationPermissionGranted = locationHelper.isLocationPermissionGranted
    val showPermissionDialog = locationHelper.showPermissionDialog

    if (locationState.value?.latitude == null || locationState.value?.longitude == null) {
        LaunchedEffect(Unit) {
            while (true) {
                locationHelper.fetchLocation()
                Log.d("checked lidnn" , "")
                delay(1000)
            }
        }
    }

    if (showPermissionDialog.value) {
        PermissionDialog(
            onDismiss = { showPermissionDialog.value = false },
            onConfirm = {
                showPermissionDialog.value = false
                locationHelper.requestPermission()
            }
        )
    }
    MainScreen(
        locationState = locationState,
        fetchLocation = {
            locationRequest.invoke()
            locationHelper.checkGpsAndFetchLocation()
            navController.navigate(CheckupScreens.ClientListScreen.name)
                        },
        isLocationPermissionGranted = isLocationPermissionGranted
    )
}

@Composable
fun MainScreen(
    locationState: MutableState<Location?>,
    fetchLocation: () -> Unit,
    isLocationPermissionGranted: MutableState<Boolean>
) {
    Column {
        if (!isLocationPermissionGranted.value) {
            Text(text = "Location permission is required")
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            locationState.value.let { location ->
                Text(text = "Latitude: ${location?.latitude}, Longitude: ${location?.longitude}")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    fetchLocation.invoke()
                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(top = 10.dp)
                    .height(45.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1F4037) , contentColor = Color.White),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = "Continue To Book ")
            }
        }
    }

}


@Composable
fun PermissionDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Permission Required") },
        text = { Text(text = "This app requires location permission to function correctly. Please grant the permission.") },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Grant Permission")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

