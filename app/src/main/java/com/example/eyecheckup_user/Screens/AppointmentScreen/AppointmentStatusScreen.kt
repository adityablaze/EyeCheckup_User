package com.example.eyecheckup_user.Screens.AppointmentScreen

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.eyecheckup_user.Screens.ClientListScreen.ClientViewModel
import com.example.eyecheckup_user.model.M_Appointment
import com.example.eyecheckup_user.model.M_Client
import com.example.eyecheckup_user.naviation.CheckupScreens
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID


@Composable
fun AppointmentStatusScreen(
    navController: NavController,
    appointmentId : String,
    appointmentViewmodel: AppointmentViewmodel = hiltViewModel(),
    clientViewModel: ClientViewModel = hiltViewModel()
){
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    var appointment : M_Appointment = M_Appointment()
    val appointments = appointmentViewmodel.Appointment.value.data

    if (!appointments?.isEmpty()!!) {
        appointment = appointments.filter {
            it.bookingId == appointmentId
        }.get(0)
    }
    val changeDialog = remember { mutableStateOf(false) }

    Surface(modifier = Modifier.fillMaxSize()) {
        if (changeDialog.value){
            ChangeContactDialog(
                appointment = appointment ,
                onDismiss = { changeDialog.value = false } ,
                onConfirm = { contactNumber , contactMail ->
                    appointment.UsercontactNumber = contactNumber
                    appointment.UsercontactMail = contactMail
                }
            )
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(modifier = Modifier
                .height(125.dp)
                .fillMaxWidth()
                .background(color = Color(0xFF1F4037))
            ) {

            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .height(45.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "",
                    tint = Color.White,
                    modifier = Modifier.clickable { navController.popBackStack() }
                )
                Text(
                    text = "Appointment Details" ,
                    color = Color.White ,
                    fontSize = 20.sp ,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Row(
                modifier = Modifier.height(120.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center
                ){
                    Image(
                        painter = rememberImagePainter(data = appointment.ImageUrl.toString()),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .border(
                                1.dp,
                                color = Color(0xFF1F4138),
                                shape = RoundedCornerShape(15.dp)
                            )
                            .clip(shape = RoundedCornerShape(16.dp))
                            .width(110.dp)
                            .background(Color(0xFFD6E6DB))
                            .align(Alignment.End)
                            .height(110.dp)
                    )
                }
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 7.dp, start = 10.dp)
                ){
                    Row(modifier = Modifier
                        .fillMaxWidth()) {
                        Column(){
                            Text(
                                text = appointment.doctorName.toString() ,
                                fontSize = 23.sp ,
                                color = Color.White
                            )
                            Text(
                                text = "Eye Doctor" ,
                                color = Color.White
                            )
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

                            }
                        }
                    }
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {

                    Spacer(modifier = Modifier.height(10.dp))
                    Card(
                        elevation = CardDefaults.cardElevation(2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(85.dp)
                            .padding(top = 15.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF4F6F5))
                    ) {
                        Column(modifier = Modifier
                            .fillMaxSize()
                            .padding(5.dp),
                        ) {
                            Text(text = "Doctor's Contact Info" , color = Color(0xFF1F4037) , fontSize = 17.sp, modifier = Modifier.offset(x = 5.dp ))
                            Text(text = appointment.ClientcontactNumber.toString() , maxLines = 1 , fontSize = 12.sp , lineHeight = 1.sp, modifier = Modifier.offset(x = 5.dp ))
                            Text(text = appointment.ClientcontactMail.toString() , maxLines = 1 , fontSize = 12.sp , lineHeight = 1.sp, modifier = Modifier.offset(x = 5.dp ))
                        }
                    }
                    Card(
                        elevation = CardDefaults.cardElevation(2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(85.dp)
                            .padding(top = 15.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF4F6F5))
                    ) {
                        Column(modifier = Modifier
                            .fillMaxSize()
                            .padding(5.dp),
                        ) {
                            Text(text = "Doctor's Address" , color = Color(0xFF1F4037) , fontSize = 17.sp, modifier = Modifier.offset(x = 5.dp ))
                            Text(text = appointment.Clientaddress.toString() , maxLines = 2 , fontSize = 12.sp , lineHeight = 13.sp, modifier = Modifier.offset(x = 5.dp ))
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Card(
                        elevation = CardDefaults.cardElevation(2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(85.dp)
                            .padding(top = 5.dp, bottom = 10.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF4F6F5))
                    ) {
                        Box(modifier = Modifier.fillMaxSize())
                        {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(end = 10.dp, top = 5.dp)
                            )  {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "",
                                    tint = if (appointment.BookingState.toString() == "Requested") Color(0xFF1F4037) else Color.Transparent.copy(alpha = 0.2f),
                                    modifier = Modifier
                                        .align(Alignment.End)
                                        .clickable {
                                            if (appointment.BookingState.toString() == "Requested") {
                                               changeDialog.value = true
                                            }
                                        }
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(5.dp)
                            ) {
                                Text(
                                    text = "Your Contact Info",
                                    color = Color(0xFF1F4037),
                                    fontSize = 17.sp,
                                    modifier = Modifier.offset(x = 5.dp )
                                )
                                Text(
                                    text = appointment.UsercontactNumber.toString(),
                                    maxLines = 1,
                                    fontSize = 12.sp,
                                    lineHeight = 1.sp,
                                    modifier = Modifier.offset(x = 5.dp )
                                )
                                Text(
                                    text = appointment.UsercontactMail.toString(),
                                    maxLines = 1,
                                    fontSize = 12.sp,
                                    lineHeight = 1.sp,
                                    modifier = Modifier.offset(x = 5.dp )
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(modifier = Modifier
                        .fillMaxWidth()
                        .height(85.dp)
                        .padding(top = 5.dp, bottom = 10.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF4F6F5)),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Box(modifier = Modifier.fillMaxSize())
                        {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(end = 10.dp, top = 5.dp)
                            )  {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "",
                                    tint = if (appointment.BookingState.toString() == "Requested") Color(0xFF1F4037) else Color.Transparent.copy(alpha = 0.2f),
                                    modifier = Modifier
                                        .align(Alignment.End)
                                        .clickable {
                                            if (appointment.BookingState.toString() == "Requested") {

                                            }
                                        }
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(5.dp)
                            ) {
                                Text(
                                    text = if (appointment.BookingState.toString() == "Confirmed") "Allotted TimeSlot"
                                           else if (appointment.BookingState.toString() == "Requested") "Selected TimeSlot"
                                           else if (appointment.BookingState.toString() == "Cancelled") "Selected TimeSlot"
                                           else if (appointment.BookingState.toString() == "Completed") "Allotted TimeSlot"
                                           else { "" },
                                    color = Color(0xFF1F4037),
                                    fontSize = 20.sp,
                                )
                                Text(
                                    text = appointment.Priority.toString(),
                                    modifier = Modifier.padding(start = 5.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(35.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp)
                            .clip(RoundedCornerShape(15.dp))
                            .background(color = Color(0xFF1E4037)),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Appointment State: ${appointment.BookingState.toString()}",
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChangeContactDialog(
    appointment: M_Appointment,
    onDismiss: () -> Unit = {},
    onConfirm: (String , String) -> Unit
) {
    val contactNumber = remember { mutableStateOf("") }
    val contactMail = remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = { onDismiss.invoke() },
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f),
        confirmButton = {
            Column(modifier = Modifier.fillMaxSize()) {
                PhonenumberInput(
                    contact_Number = appointment.UsercontactNumber.toString(),
                    onChange = {contactNumber.value= it}
                )
                EmailInput(
                    contact_Mail = appointment.UsercontactMail.toString(),
                    onChange = {contactMail.value= it}
                )
                Button(
                    onClick = {
                        onConfirm(contactNumber.value , contactMail.value)
                    }
                ) {
                    Text(text = "Confirm")
                }
            }
        }
    )
}
