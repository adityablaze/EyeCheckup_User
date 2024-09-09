package com.example.eyecheckup_user.Screens.VisionScreens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.example.eyecheckup_user.Components.AppBar
import com.example.eyecheckup_user.Components.CameraPermission
import com.example.eyecheckup_user.R
import com.example.eyecheckup_user.Components.ShowDialog
import com.example.eyecheckup_user.Components.inchToSp
import com.example.eyecheckup_user.naviation.CheckupScreens
import kotlinx.coroutines.launch

@Composable
fun VisionViewScreen(
    navController: NavController,
    visionScore : List<Int?>,
    onClear_Score : () -> Unit
) {
    val randomChar = remember { mutableStateOf("") }
    LaunchedEffect(Unit){ randomChar.value = getRandomCharacter().toString() }
    val distance = remember { mutableStateOf(0f) }
    val instructState = remember { mutableStateOf(false) }
    val imageState = remember { mutableStateOf(0) }
    val instruct = remember { mutableStateOf("") }
    val showDialog = remember { mutableStateOf(false) }
    val font_size = setOf<Double>( 0.3800 , 0.3400 , 0.2900 , 0.2588 , 0.2155 , 0.1788 , 0.126 , 0.070 , 0.049 , 0.0228)
    var enabled = if (distance.value >= 30.00  && distance.value <= 200.00) true else false
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(Unit) {
        if (visionScore.size == 0){
            instruct.value = "You need to cover your Left Eye"
            instructState.value = true
            imageState.value = R.drawable.eyecover
        }
        if (visionScore.size == 10){
            instruct.value = "You need to cover your Right Eye"
            instructState.value = true
            imageState.value = R.drawable.eyecover
        }
    }
    Box(modifier = Modifier.fillMaxSize())
    {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = distance.value.toString())
            if (visionScore.size <= 10) {
                Text(text = "Please Cover Your Right Eye")
            }else{
                Text(text = "Please Cover Your Left Eye")
            }
            Spacer(modifier = Modifier.height(70.dp))
            Text(
                text = randomChar.value,
                fontSize = inchToSp(Inch =
                if (visionScore.size < 10) {
                    font_size.toList().get(visionScore.size)
                }else{
                    font_size.toList().get(visionScore.size - 10)
                }
                ),
            )
            Spacer(modifier = Modifier.height(70.dp))
            Button(
                onClick = {
                    if (enabled) {
                        enabled = !enabled
                        lifecycleOwner.lifecycleScope.launch {
                            kotlinx.coroutines.delay(500)
                            navController.navigate(CheckupScreens.VisionTestScreen.name + "/${randomChar.value}"){
                                popUpTo(CheckupScreens.HomeScreen.name){
                                    inclusive = false
                                }
                            }
                        }
                    }
                          },
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(40.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1F4037) , contentColor = Color.White),
                shape = RoundedCornerShape(10.dp)

            ) {
                Text(text = "Next")
            }
        }
        AppBar(
            imageVector = Icons.Default.ArrowBack,
            title = "Vision Test",
            backEnabled = false
        ) {
            showDialog.value = true
        }
        Column( modifier = Modifier
            .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.Start
        ) {
            CameraPermission(
                modifier = Modifier.fillMaxSize(0.2f),
                distance = { distance.value = it },
            )
        }
        BackHandler(onBack = {
          showDialog.value = !showDialog.value
        })
        if (showDialog.value) {
            ShowDialog(
                onDismiss = { showDialog.value = false },
                title = "Do you want to exit this test" ,
                onConfirm = {
                    navController.navigate(CheckupScreens.HomeScreen.name){
                        popUpTo(CheckupScreens.HomeScreen.name){
                            inclusive = false
                        }
                    }
                    showDialog.value = false
                }
            )
        }
        if (instructState.value == true) {
            Instruct(
                text = instruct.value ,
                image = imageState.value,
                onDismiss = {instructState.value = false})
        }
    }
}

fun getRandomCharacter(
    exclusions :Set<Char> = emptySet()
): Char {
    val alphabet = ('A'..'Z').filterNot { it in exclusions }
    return alphabet.random()
}

@Composable
fun Instruct(
    text: String,
    image: Int,
    onDismiss: () -> Unit
){
    AlertDialog(
        onDismissRequest = { onDismiss.invoke() },
        confirmButton = {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = text)
                Image(
                    painter = painterResource(id = image),
                    contentDescription = "",
                    modifier = Modifier
                        .height(100.dp)
                        .width(200.dp)
                        .clip(RoundedCornerShape(40.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = { onDismiss.invoke() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1F4037) , contentColor = Color.White),
                ) {
                    Text(text = "Continue")
                }
            }

        },
        containerColor = Color(0xFFD6E6DA),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
    )
}