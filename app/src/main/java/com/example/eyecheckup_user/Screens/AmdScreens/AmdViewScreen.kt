package com.example.eyecheckup_user.Screens.AmdScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.eyecheckup_user.R
import com.example.eyecheckup_user.naviation.CheckupScreens

@Preview
@Composable
fun AmdViewScreen(
    navController: NavController = NavController(LocalContext.current)
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Look At the Grid given below and \n focus on the dot in the centre" ,
            textAlign = TextAlign.Center
            )
        Image(
            painter = painterResource(id = R.drawable.hjj),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.5f)
        )
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = "Look At the Grid thoroughly and press Continue>>",
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(40.dp))
        Button(
            onClick = { navController.navigate(CheckupScreens.AmdTestScreen.name) },
            modifier = Modifier.fillMaxWidth(0.8f).height(40.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1F4037) , contentColor = Color.White),
            shape = RoundedCornerShape(10.dp)

        ) {
            Text(text = "Continue")
        }

    }
}