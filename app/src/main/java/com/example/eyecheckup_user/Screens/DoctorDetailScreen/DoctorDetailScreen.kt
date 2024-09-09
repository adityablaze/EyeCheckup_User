package com.example.eyecheckup_user.Screens.DoctorDetailScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.eyecheckup_user.Screens.ClientListScreen.ClientViewModel
import com.example.eyecheckup_user.model.M_Client
import com.example.eyecheckup_user.naviation.CheckupScreens
import com.google.accompanist.systemuicontroller.rememberSystemUiController


@Composable
fun DoctorDetailScreen(
    navController: NavController = NavController(LocalContext.current),
    arguments : String,
    clientViewModel: ClientViewModel = hiltViewModel()
){
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val scrH = LocalConfiguration.current.screenHeightDp
    val scrW = LocalConfiguration.current.screenWidthDp
    val clientid = arguments.split("|").get(0)
    val distance = arguments.split("|").get(1)
    val client_ = clientViewModel.clients.value.data?.toList()?.filter { client ->
        client.uid.toString() == clientid
    }
    var client = M_Client()

    if (!client_?.isEmpty()!!) {
        client = client_.get(0)
    }
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(
        color = Color(0xFF1F4037),
        darkIcons = false // Adjust based on your status bar content color
    )
    val detailReviewState = remember { mutableStateOf(false) }
    val detailReview = remember { mutableStateOf("") }

    Surface(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())) {
        if (detailReviewState.value) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
               AlertDialog(
                   containerColor = Color(0xFFD6E6DA),
                   modifier = Modifier
                       .fillMaxHeight(0.4f)
                       .fillMaxWidth(0.8f)
                   ,
                   onDismissRequest = { detailReviewState.value = !detailReviewState.value },
                   confirmButton = {
                       Column(modifier = Modifier
                           .fillMaxSize()
                           .verticalScroll(rememberScrollState()) , verticalArrangement = Arrangement.Top , horizontalAlignment = Alignment.Start) {
                           Text(text = detailReview.value , color = Color.Black)
                       }
                   }
               )
            }
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(modifier = Modifier
                .height(0.17 * screenHeight)
                .fillMaxWidth()
                .background(color = Color(0xFF1F4037))) {

            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = screenHeight / 49,
                    bottom = screenHeight / 49,
                    end = screenWidth / 24,
                    start = screenWidth / 24
                ),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(horizontalArrangement = Arrangement.SpaceBetween ,modifier = Modifier
                .fillMaxWidth()
                .height(0.065 * screenHeight)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "",
                    tint = Color.White,
                    modifier = Modifier.clickable { navController.popBackStack() }
                )
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "" , tint = Color.White)
            }
            Row(
                modifier = Modifier.height(0.16 * screenHeight),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .fillMaxHeight(), verticalArrangement = Arrangement.Center
                ){
                     Image(
                         painter = rememberImagePainter(data = client.imageUrl.toString()),
                         contentDescription = "",
                         contentScale = ContentScale.Crop,
                         modifier = Modifier
                             .border(
                                 1.dp,
                                 color = Color(0xFF1F4138),
                                 shape = RoundedCornerShape(15.dp)
                             )
                             .clip(shape = RoundedCornerShape(16.dp))
                             .size(0.15 * screenHeight)
                             .background(Color(0xFFD6E6DB))
                             .align(Alignment.End)
                         )
                }
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = (scrH / 105.5).dp, start = (scrW / 36).dp)
                ){
                    Row(modifier = Modifier
                        .fillMaxWidth()) {
                        Column(){
                            Text(text = client.doctorName.toString() , fontSize = 23.sp , color = Color.White)
                            Text(text = "Eye Doctor" , color = Color.White)
                        }
                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Bottom
                        )
                        {
                            Column(modifier = Modifier.align(Alignment.End)) {
                                Text(text = "Fee", modifier = Modifier.align(Alignment.Start))
                                Text(
                                    text = "Rs.${client.fee.toString()}",
                                    modifier = Modifier.align(Alignment.End)
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(0.16 / 6 * screenHeight))
            Row(
                modifier = Modifier
                    .height(screenHeight / 7)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement =Arrangement.SpaceBetween
            ) {
                Card(
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(
                            top = screenHeight / 73,
                            bottom = screenHeight / 73,
                            end = screenWidth / 36
                        )
                        .weight(1f),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF4F6F5))
                ) {
                    Column(modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "" ,
                            tint = Color(0xFF1F4037),
                            modifier = Modifier.size(33.dp))
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = "${client.experience}  Yrs+"  , fontSize = 14.sp, lineHeight = 1.sp)
                        Text(text = "Experience", fontSize = 10.sp, lineHeight = 1.sp)
                    }
                }
                Card(
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(
                            top = screenHeight / 73,
                            bottom = screenHeight / 73,
                            end = screenWidth / 72,
                            start = screenWidth / 72
                        )
                        .weight(1f),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF4F6F5))
                ) {
                    Column(modifier = Modifier
                        .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Star,
                            contentDescription = "",
                            tint = Color(0xFF1F4037),
                            modifier = Modifier.size(31.dp)
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = client.rating.toString() , fontSize = 14.sp, lineHeight = 1.sp)
                        Text(text = "Rating", fontSize = 10.sp, lineHeight = 1.sp)
                    }
                }
                Card(
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(
                            top = screenHeight / 73,
                            bottom = screenHeight / 73,
                            start = screenWidth / 36
                        )
                        .weight(1f),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF4F6F5))
                ) {
                    Column(modifier = Modifier
                        .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "" ,
                            tint = Color(0xFF1F4037),
                            modifier = Modifier.size(30.dp))
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = "9AM-11PM" , fontSize = 14.sp , lineHeight = 1.sp)
                        Text(text = "Timing", fontSize = 10.sp , lineHeight = 1.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(screenHeight / 73))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height((scrH / 8.68).dp)
                    .padding(top = screenHeight / 73, bottom = screenHeight / 146),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF4F6F5)),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp),
                ) {
                    Text(text = client.clinicName.toString(), fontSize = 18.sp , modifier = Modifier.padding(4.dp))
                    Text(
                        text = client.address.toString(),
                        maxLines = 2,
                        fontSize = 12.sp,
                        lineHeight = 12.sp,
                        modifier = Modifier.offset(x = 4.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(3.dp))
            Card(
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height((scrH / 8.68).dp)
                    .padding(top = screenHeight / 146, bottom = screenHeight / 73),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF4F6F5))
            ) {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp),
                ) {
                    Text(text = "Contact Us" , fontSize = 17.sp , modifier = Modifier.padding(4.dp))
                    Text(text = client.contactNumber.toString() , maxLines = 1 , fontSize = 12.sp , lineHeight = 1.sp, modifier = Modifier.offset(x = 4.dp ))
                    Text(text = client.contactMail.toString() , maxLines = 1 , fontSize = 12.sp , lineHeight = 1.sp, modifier = Modifier.offset(x = 4.dp ))
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height((scrH / 24.6).dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Reviews" , fontSize = 22.sp ,  modifier = Modifier.offset(x = 7.dp))
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(elevation = 2.dp, shape = RoundedCornerShape(12.dp))
                    .clip(RoundedCornerShape(12.dp))
                    .height((scrH / 6.15).dp)
                    .background(color = Color(0xFFF4F6F5))
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                client.reviews?.forEach {
                    Card(modifier = Modifier
                        .fillMaxHeight()
                        .padding(5.dp)
                        .clickable {
                            detailReview.value = it.toString()
                            detailReviewState.value = true
                        }
                        .width((scrW / 3.6).dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFD6E6DA))
                    ) {
                        Column (modifier = Modifier
                            .padding(3.dp)
                            .fillMaxSize()
                        ){
                            Text(text = it.toString() , fontSize = 13.sp , lineHeight = 14.sp , overflow = TextOverflow.Ellipsis)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height((scrH / 34.9).dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height((scrH / 13.41).dp)
                    .clip(RoundedCornerShape(15.dp))
                    .clickable {
                        navController.navigate(CheckupScreens.AppointmentScreen.name + "/${client.uid.toString()}|${client.distance.toString()}")
                    }
                    .background(color = Color(0xFF1E4037)),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Make An Appointment" , color = Color.White )
            }
            
        }
    }
}

