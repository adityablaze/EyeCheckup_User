package com.example.eyecheckup_user.Screens.AppointmentScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.eyecheckup_user.Components.AppBar
import com.example.eyecheckup_user.model.M_Appointment
import com.example.eyecheckup_user.naviation.CheckupScreens
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppointmentListScreen(
    navController: NavController,
    appointmentViewmodel: AppointmentViewmodel = hiltViewModel(),
){
    val Appointments = appointmentViewmodel.Appointment.value.data?.toList()
    var appointments : List<M_Appointment> = emptyList()
    if (Appointments != null) {
           appointments = Appointments.filter {
              it.Userid.toString() == FirebaseAuth.getInstance().uid
           }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())) {
        AppBar(title = "Your Appointments", backEnabled = false , imageVector = Icons.Default.ArrowBack) {
            navController.navigate(CheckupScreens.HomeScreen.name)
        }
        appointments.forEach { appoint ->
            AppointmentContent(appoint){
                navController.navigate(CheckupScreens.AppointmentStatusScreen.name + "/${appoint.bookingId}")
            }
        }
    }
}

@Composable
fun AppointmentContent(
    appoint: M_Appointment,
    onClick: () -> Unit
) {
    val scrH = LocalConfiguration.current.screenHeightDp
    val scrW = LocalConfiguration.current.screenWidthDp
    Card(
        modifier = Modifier
            .height((scrH / 3.075).dp)
            .fillMaxWidth()
            .padding(top = (scrH / 105.42).dp, start = (scrW / 72).dp, end = (scrW / 72).dp)
            .clickable { onClick.invoke() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFD6E6DA))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = (scrH / 73.8).dp,
                    bottom = (scrH / 73.8).dp,
                    start = (scrW / 36).dp,
                    end = (scrW / 36).dp
                )
        ) {
            Row(modifier = Modifier.fillMaxWidth().padding(start = (scrW / 72).dp)) {
                Text(text = "Appointment Status:")
                Text(
                    text = " ${appoint.BookingState.toString()}",
                    color = if (appoint.BookingState.toString() == "Confirmed") { Color(0xFF27C70B) }
                    else if (appoint.BookingState.toString() == "Requested"){ Color(0xFF1F4037)
                    }
                    else if (appoint.BookingState.toString() == "Cancelled"){ Color(0xFFA81E33)
                    }
                    else if (appoint.BookingState.toString() == "Completed"){ Color(0xFF3D4643)
                    }
                    else{ Color.Transparent }
                )
            }
            Spacer(modifier = Modifier.height((scrH / 147.6).dp))
            Card(
                elevation = CardDefaults.cardElevation(0.5.dp) ,
                colors = CardDefaults.cardColors(containerColor = Color(0xFFD6E6DA))
            ) {
                Text(text = "Doctor Info", modifier = Modifier.padding(start = (scrW / 72).dp))
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = (scrH / 147.6).dp,
                        bottom = (scrH / 147.6).dp,
                        start = (scrW / 72).dp,
                        end = (scrW / 72).dp
                    )) {
                    Column(modifier = Modifier.width((scrW / 3.6).dp)) {
                        Image(
                            painter = rememberImagePainter(data = appoint.ImageUrl),
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(15.dp))
                                .border(
                                    1.dp,
                                    color = Color(0xFF1F4037),
                                    shape = RoundedCornerShape(15.dp)
                                )
                        )
                    }
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = appoint.doctorName.toString(),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 18.sp,
                            lineHeight = 1.sp,
                            fontWeight = FontWeight.Normal
                        )
                        Text(
                            text = appoint.clinicName.toString(),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal
                        )
                        Text(
                            text = appoint.Clientaddress.toString(),
                            maxLines = 2,
                            lineHeight = 15.sp,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 15.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height((scrH / 73.8).dp))
            Card(
                elevation = CardDefaults.cardElevation(0.5.dp) ,
                colors = CardDefaults.cardColors(containerColor = Color(0xFFD6E6DA)),
                modifier = Modifier.height((scrH / 14.76).dp)
            ) {
                Text(text = "Slot Info" , modifier = Modifier.padding(start = (scrW / 72).dp))
                Row (
                    modifier = Modifier.fillMaxSize().padding(start = (scrW / 72).dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(text = appoint.Priority.toString())
                }
            }
        }
    }
}