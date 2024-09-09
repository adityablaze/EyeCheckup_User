package com.example.eyecheckup_user.Screens.AmdScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.eyecheckup_user.Components.AppBar
import com.example.eyecheckup_user.naviation.CheckupScreens

@Composable
fun AmdResultScreen(
    navController: NavController = NavController(LocalContext.current),
    result : String = "lot"
){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column {
            AppBar(title = "Test Results", backEnabled = false , imageVector = Icons.Default.ArrowBack) {
                navController.navigate(CheckupScreens.HomeScreen.name)
            }
        }
        Column(
            modifier = Modifier.fillMaxSize() ,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally)
        {
            if (result == "no") {
                Text(
                    text = "You Might Not have AMD but \n Consulting a doctor is won't hurt",
                    textAlign = TextAlign.Center
                )
            }
            else if (result == "some") {
                Text(
                    text = "You Might have AMD you should \n Consult a doctor!!!",
                    textAlign = TextAlign.Center
                )
            }
            else if (result == "lot") {
                Text(
                    text = "You are at high risk of having  AMD!! \n you should  Consult a doctor!!!",
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
            Button(
                onClick = {
                    navController.navigate(CheckupScreens.LocationScreen.name)
                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(top = 10.dp)
                    .height(45.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1F4037) , contentColor = Color.White),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = "Book An Appointment")
            }
        }

    }
}