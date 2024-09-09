package com.example.eyecheckup_user.Screens.HomeScreen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.eyecheckup_user.R
import com.example.eyecheckup_user.naviation.CheckupScreens
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    navController: NavController =  NavController(LocalContext.current),
    onClear_Score : () -> Unit
) {
    val pageCount = 3
    val pagerState = rememberPagerState(initialPage = 0 , pageCount = { pageCount })
    val checkupState = remember { mutableStateOf( 0 ) }

    LaunchedEffect(pagerState.currentPage) {
        checkupState.value = pagerState.currentPage
    }
    LaunchedEffect( checkupState.value) {
        pagerState.scrollToPage(checkupState.value.toInt())
    }

    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(
        color = Color(0xFF1F4037),
        darkIcons = false // Adjust based on your status bar content color
    )
    val shift = remember{
        Animatable(90f)
    }
    LaunchedEffect(key1 = true){
        shift.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 800)
        )
    }
    val transparency = remember{
        Animatable(0f)
    }
    LaunchedEffect(key1 = true){
        transparency.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 2000)
        )
    }
    Box(modifier = Modifier
        .fillMaxSize()
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Top section with the logo and wave
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .offset(y = -310.dp)
            ) {
                drawIntoCanvas {
                    // Draw wave
                    val path = Path().apply {
                        moveTo(0f, size.height * 0.7f)
                        cubicTo(
                            size.width * 0.2f, size.height * 0.6f,
                            size.width * 0.8f, size.height * 0.8f,
                            size.width, size.height * 0.7f
                        )
                        lineTo(size.width , 0f)
                        lineTo(0f, 0f)
                        close()
                    }
                    drawPath(path = path, color = Color(0xFF99E197).copy(alpha = transparency.value))
                }
            }
        }
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Display the HorizontalPager
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                PageContent(page , navController , { onClear_Score.invoke() })
            }
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier
                .height(125.dp)
                .fillMaxWidth())
            Column(modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(topEnd = 10.dp, topStart = 10.dp))
            ) {
                BottomBar(checkupState)
            }
        }
    }
}

@Composable
fun PageContent(
    page: Int ,
    navController: NavController,
    onClear_Score: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (page == 0 ){
            CheckUpContent(navController = navController, onClear_Score = { onClear_Score.invoke() })
        }
        else if (page == 1 ){
            ExerciseContent(navController = navController)
        }
        else{
            AccountContent(navController = navController)
        }
    }
}

@Composable
fun AccountContent(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ){
        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(120.dp))
            Icon(
                painter = painterResource(id = R.drawable.account),
                contentDescription = "",
                modifier = Modifier
                    .size(150.dp),
                tint = Color.Black
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = FirebaseAuth.getInstance().currentUser?.phoneNumber.toString())
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    navController.navigate(CheckupScreens.AppointmentListScreen.name)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF78BD76)),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = "My Appointment")
            }
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = {
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF78BD76)),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = "My Info")
            }
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate(CheckupScreens.LoginScreen.name)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF78BD76)),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Row {
                    Text(text = "Logout")
                    Icon(imageVector = Icons.Default.ExitToApp, contentDescription = "")
                }

            }
        }
    }

}

@Composable
fun ExerciseContent(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ){
        Spacer(modifier = Modifier.height(185.dp))
        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
        ) {
              ExerciseCard(
                  imageid = R.drawable.eye,
                  exercise = "Vision Exercise",
                  description = "",
                  onClick = {

                  }
              )
        }
    }
}

@Composable
fun ExerciseCard(
    imageid: Int,
    exercise: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .clickable { onClick.invoke() }
            .height(100.dp)
            .fillMaxWidth()
            .padding(6.dp),
        colors = CardDefaults.cardColors(Color(0xFFD6E6DA))
    ) {
        Row(modifier = Modifier
            .fillMaxSize()
            .padding(9.dp)
        ) {
            Column {
                Image(
                    painterResource(id = imageid) ,
                    contentDescription = "" ,
                    Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(30.dp)),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.padding(4.dp)) {
                Text(text = exercise , fontSize = 23.sp)
                Text(text = description)
            }
        }
    }
}

@Composable
fun BottomBar(
    checkupState: MutableState<Int>
) {
    val offset = remember{ Animatable(0f) }
    LaunchedEffect(checkupState.value) {
        if (checkupState.value == 0){
            offset.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 800)
            )
        }
        else if (checkupState.value == 1){
            offset.animateTo(
                targetValue =   0.325f,
                animationSpec = tween(durationMillis = 800)
            )
        }
        else{
            offset.animateTo(
                targetValue =   0.671f,
                animationSpec = tween(durationMillis = 800)
            )
        }
    }
    Box(modifier = Modifier.fillMaxSize())
    {
        Column(
            modifier = Modifier.fillMaxSize() ,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Canvas(
                modifier = Modifier
                    .height(20.dp)
                    .fillMaxWidth()
                    .background(Color.Transparent)
            ) {
                drawCubicCurve(
                    bottomLeft = Offset(size.width * (0.01f + offset.value), size.height),
                    topRight = Offset( size.width * (0.3f + offset.value), 0f),
                    color = Color(0xFF99E197)
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                    .background(color = Color(0xFF99E197))

            ) {

            }
        }

        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier .clickable { checkupState.value = 0 }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.eye),
                    contentDescription = "",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Text(text = "Tests" , color = Color.White )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier .clickable { checkupState.value = 1 }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.eye),
                    contentDescription = "",
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(50.dp),
                    contentScale = ContentScale.Crop
                )
                Text(text = "Exercise" , color = Color.White )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier .clickable { checkupState.value = 2 }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.account),
                    contentDescription = "",
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(55.dp),
                    contentScale = ContentScale.Crop
                )
                Text(text = "Account" , color = Color.White)
            }

        }
    }
}

@Composable
fun CheckUpContent(
    navController: NavController,
    onClear_Score: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ){
        Spacer(modifier = Modifier.height(230.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .verticalScroll(rememberScrollState())
        ) {
            DiseaseCard(
                imageid = R.drawable.eye,
                disease = "Vision Test",
                description = "",
                onClick = {
                    navController.navigate(CheckupScreens.VisionViewScreen.name)
                    onClear_Score.invoke()
                }
            )
            DiseaseCard(
                imageid = R.drawable.download,
                disease = "AMD",
                description = "",
                onClick = { navController.navigate(CheckupScreens.AmdViewScreen.name) }
            )
            DiseaseCard(
                imageid = R.drawable.download,
                disease = "AMD",
                description = "",
                onClick = { navController.navigate(CheckupScreens.AmdViewScreen.name) }
            )
            DiseaseCard(
                imageid = R.drawable.download,
                disease = "AMD",
                description = "",
                onClick = { navController.navigate(CheckupScreens.AmdViewScreen.name) }
            )
            DiseaseCard(
                imageid = R.drawable.download,
                disease = "Astagmatism",
                description = "",
                onClick = { navController.navigate(CheckupScreens.AstagmatismViewScreen.name) }
            )
            DiseaseCard(
                imageid = R.drawable.download,
                disease = "Color Blindness",
                description = "",
                onClick = { }
            )
        }
    }

}


@Composable
fun DiseaseCard(
    imageid : Int,
    disease : String,
    description:String,
    onClick:() -> Unit,
    modifier: Modifier = Modifier
){
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = modifier
            .clickable { onClick.invoke() }
            .height(100.dp)
            .fillMaxWidth()
            .padding(6.dp),
        colors = CardDefaults.cardColors(Color(0xFFD6E6DA))
    ) {
        Row(modifier = modifier
            .fillMaxSize()
            .padding(9.dp)
        ) {
            Column {
                Image(
                    painterResource(id = imageid) ,
                    contentDescription = "" ,
                    Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(30.dp)),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.padding(4.dp)) {
                Text(text = disease , fontSize = 23.sp)
                Text(text = description)
            }
        }
    }
}

fun DrawScope.drawCubicCurve(
    bottomLeft: Offset,
    topRight: Offset,
    color: Color = Color.White
) {
    val x1Modifier = 0.31f
    val x2Modifier = 0.22f
    val x3Modifier = 0.5f
    val y1Modifier = 0f
    val y2Modifier = 1f
    val y3Modifier = 1f

    val delta = topRight.minus(bottomLeft)
    val width = delta.x
    val height = delta.y

    val path = Path()
    path.moveTo(bottomLeft.x, bottomLeft.y)
    path.cubicTo(
        x1 = bottomLeft.x.plus(width.times(x1Modifier)), y1 = bottomLeft.y.plus(height.times(y1Modifier)),
        x2 = bottomLeft.x.plus(width.times(x2Modifier)), y2 = topRight.y.times(y2Modifier),
        x3 = bottomLeft.x.plus(width.times(x3Modifier)), y3 = topRight.y.times(y3Modifier),
    )
    path.cubicTo(
        x1 = topRight.x.minus(width.times(x2Modifier)), y1 = topRight.y.times(y2Modifier),
        x2 = topRight.x.minus(width.times(x1Modifier)), y2 = bottomLeft.y.plus(height.times(y1Modifier)),
        x3 = topRight.x, y3 = bottomLeft.y,
    )
    drawPath(
        path = path,
        alpha = 0.5f,
        brush = Brush.verticalGradient(
            colors = listOf(
                color.copy(alpha = 0.0f),
                color.copy(alpha = 0.7f),
                color
            ), startY = bottomLeft.y, endY = topRight.y
        )
    )
    drawPath(
        color = color,
        path = path,
    )
}

@Composable
fun AccountSlider(
    navController: NavController,
    onDissmiss: () -> Unit
) {
    Row(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.6f)
                .background(color = Color.Transparent.copy(alpha = 0.5f))
                .clip(RoundedCornerShape(topEnd = 10.dp))
                .background(color = Color(0xFFD6E6DA)),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, top = 10.dp)){
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = ""  , modifier = Modifier.clickable { onDissmiss.invoke() })
            }
            Spacer(modifier = Modifier.height(20.dp))
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "",
                modifier = Modifier.size(100.dp)
            )
            Text(text = FirebaseAuth.getInstance().currentUser?.phoneNumber.toString())
            Spacer(modifier = Modifier.height(40.dp))
            Button(
                onClick = {
                    navController.navigate(CheckupScreens.AppointmentListScreen.name)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1F4037)),
                modifier = Modifier.fillMaxWidth(0.9f),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = "My Appointment")
            }
            Button(
                onClick = {
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1F4037)),
                modifier = Modifier.fillMaxWidth(0.9f),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = "My Info")
            }
            Button(
                onClick = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate(CheckupScreens.LoginScreen.name)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1F4037)),
                modifier = Modifier.fillMaxWidth(0.9f),
                shape = RoundedCornerShape(10.dp)
            ) {
                Row {
                    Text(text = "Logout")
                    Icon(imageVector = Icons.Default.ExitToApp, contentDescription = "")
                }

            }

        }
        Column(modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(color = Color.Transparent.copy(alpha = 0.5f))
            .clickable { onDissmiss.invoke() }
        ) {
        }
    }
}