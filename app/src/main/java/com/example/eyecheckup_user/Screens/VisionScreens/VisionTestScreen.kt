package com.example.eyecheckup_user.Screens.VisionScreens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.example.eyecheckup_user.Components.ShowDialog
import com.example.eyecheckup_user.naviation.CheckupScreens
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun VisionTestScreen(
    navController: NavController = NavController(LocalContext.current),
    randChar: String = "",
    visionScore : List<Int?>,
    onClick_Vision: (Int) -> Unit,
    onClear_Score : () -> Unit
){
    val char1 = randChar.toCharArray().get(0)
    val char2 = remember { mutableStateOf<Char>(' ') }
    val char3 = remember { mutableStateOf<Char>(' ') }
    val char4 = remember {mutableStateOf<Char>(' ') }
    val mySet = remember { mutableStateListOf<Char>() }

    val showDialog = remember { mutableStateOf(false) }

     LaunchedEffect(Unit){
         char2.value = getRandomCharacter(exclusions = setOf(char1))
         char3.value = getRandomCharacter(exclusions = setOf(char1 , char2.value))
         char4.value = getRandomCharacter(exclusions = setOf(char1 , char2.value , char3.value))
         mySet.addAll(listOf( char1 , char2.value , char3.value , char4.value).toMutableList().shuffled())
     }

    Box(modifier = Modifier.fillMaxSize())
    {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = visionScore.toString())
            if (mySet.size == 4) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    LetterCard(myChar = mySet.get(0), navController, visionScore) {
                        if (mySet.get(0) == char1) {
                            onClick_Vision(1)
                        } else {
                            onClick_Vision(0)
                        }
                    }
                    LetterCard(
                        myChar = mySet.get(1),
                        navController,
                        visionScore,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (mySet.get(1) == char1) {
                            onClick_Vision(1)
                        } else {
                            onClick_Vision(0)
                        }
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    LetterCard(myChar = mySet.get(2), navController, visionScore) {
                        if (mySet.get(2) == char1) {
                            onClick_Vision(1)
                        } else {
                            onClick_Vision(0)
                        }
                    }
                    LetterCard(
                        myChar = mySet.get(3),
                        navController, visionScore, modifier = Modifier.fillMaxWidth()
                    ) {
                        if (mySet.get(3) == char1) {
                            onClick_Vision(1)
                        } else {
                            onClick_Vision(0)
                        }
                    }
                }
            }
        }
        BackHandler(onBack = {
            showDialog.value = !showDialog.value
        })
        if (showDialog.value) {
            ShowDialog(
                onDismiss = { showDialog.value = false },
                title = "Do you want to exit this test",
                onConfirm = {
                    navController.navigate(CheckupScreens.HomeScreen.name) {
                        popUpTo(CheckupScreens.HomeScreen.name) {
                            inclusive = false
                        }
                    }
                    showDialog.value = false
                }
            )
        }
    }
}

@Composable
fun LetterCard(
    myChar: Char,
    navController: NavController,
    visionScore: List<Int?>,
    modifier: Modifier = Modifier,
    onClick : () -> Unit,
) {
    val enabled = remember { mutableStateOf(false) }

    LaunchedEffect(Unit){
        delay(500)
        enabled.value = !enabled.value
    }
    val lifecycleOwner = LocalLifecycleOwner.current
    Card(
        modifier = modifier
            .fillMaxWidth(0.5f)
            .padding(10.dp)
            .height(150.dp)
            .clickable {
                lifecycleOwner.lifecycleScope.launch {
                    if (enabled.value) {
                        onClick.invoke()
                        enabled.value = !enabled.value
                        delay(200)
                        if (visionScore.size == 19) {
                            navController.navigate(CheckupScreens.VisionResultScreen.name) {
                                popUpTo(CheckupScreens.HomeScreen.name) {
                                    inclusive = false
                                }
                            }
                        } else {
                            navController.navigate(CheckupScreens.VisionViewScreen.name) {
                                popUpTo(CheckupScreens.HomeScreen.name) {
                                    inclusive = false
                                }
                            }
                        }
                    }
                }

            }
    ) {
        Column(
            modifier = modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = myChar.toString(),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontSize = 100.sp
            )
        }
    }
}