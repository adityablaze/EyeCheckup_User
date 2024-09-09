package com.example.eyecheckup_user.Screens.VisionScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import com.example.eyecheckup_user.naviation.CheckupScreens

@Composable
fun VisionResultScreen(
    navController: NavController,
    visionScore : List<Int?>,
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "ResultScreen")
        Text(text = visionScore.toString())
        Button(onClick = { navController.navigate(CheckupScreens.LocationScreen.name) }) {
            Text(text = "Book An Appointment")
        }
    }

}