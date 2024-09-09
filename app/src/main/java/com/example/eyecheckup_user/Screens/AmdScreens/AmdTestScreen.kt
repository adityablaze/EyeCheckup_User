package com.example.eyecheckup_user.Screens.AmdScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.eyecheckup_user.R
import com.example.eyecheckup_user.naviation.CheckupScreens

@Composable
fun AmdTestScreen(
    navController: NavController = NavController(LocalContext.current)
){
    val expand = remember {
        mutableStateOf(false)
    }
    if (expand.value) {
        ImageGrid(onDismiss = { expand.value = !expand.value })
    }
    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.2f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.hjj),
                contentDescription = "",
                modifier = Modifier
                    .padding(top = 30.dp, bottom = 30.dp, start = 30.dp, end = 10.dp)
                    .clickable { expand.value = !expand.value }
            )
            Icon(
                imageVector = Icons.Rounded.Warning,
                contentDescription = "",
                tint = Color(0xFF721D1D)
            )
            Text(
                text = "You can Tap on the grid to enlarge it and view it",
                fontSize = 13.sp,
                lineHeight = 15.sp,
                color = Color(0xFF721D1D)
            )
        }
        val score = remember {
            mutableStateOf("")
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "What Did You Observe")
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = {
                        score.value = "no"
                        navController.navigate( CheckupScreens.AmdResultScreen.name + "/${score.value}"){
                            popUpTo(CheckupScreens.AmdViewScreen.name){
                                inclusive = true
                            }
                        }
                              },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(45.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1F4037) , contentColor = Color.White),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(text = "I Saw No Curvy Lines")
                }
                Button(
                    onClick = {
                        score.value = "some"
                        navController.navigate( CheckupScreens.AmdResultScreen.name + "/${score.value}"){
                            popUpTo(CheckupScreens.AmdViewScreen.name){
                                inclusive = true
                            }
                        }
                              },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(top = 10.dp)
                        .height(45.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1F4037) , contentColor = Color.White),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(text = "I Saw Some Curvy Lines")
                }


                Button(
                    onClick = {
                        score.value = "lot"
                        navController.navigate( CheckupScreens.AmdResultScreen.name + "/${score.value}"){
                            popUpTo(CheckupScreens.AmdViewScreen.name){
                                inclusive = true
                            }
                        }
                              },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(top = 10.dp)
                        .height(45.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1F4037) , contentColor = Color.White),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(text = "I Saw A Lot of Curvy Lines")
                }
            }

        }
    }
}

@Composable
fun ImageGrid(
    onDismiss : () -> Unit
) {
    AlertDialog(
        containerColor = Color(0xFFD6E6DA),
        onDismissRequest = { onDismiss.invoke() },
        confirmButton = {
            Column(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f),
                verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Look At the Grid given below and \n focus on the dot in the centre" ,
                    textAlign = TextAlign.Center,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(20.dp))
                Image(
                    painter = painterResource(id = R.drawable.hjj),
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxWidth()

                )
            }
        }
    )
}

