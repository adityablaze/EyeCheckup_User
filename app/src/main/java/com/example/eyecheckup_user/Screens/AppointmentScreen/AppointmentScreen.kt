package com.example.eyecheckup_user.Screens.AppointmentScreen

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.util.Log
import android.widget.DatePicker
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.eyecheckup_user.R
import com.example.eyecheckup_user.Screens.ClientListScreen.ClientViewModel
import com.example.eyecheckup_user.Screens.LoginScreen.CountryCodeDropdown
import com.example.eyecheckup_user.Screens.LoginScreen.countryCodes
import com.example.eyecheckup_user.model.M_Appointment
import com.example.eyecheckup_user.model.M_Client
import com.example.eyecheckup_user.naviation.CheckupScreens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID
import kotlin.math.sin


@Composable
fun AppointmentScreen(
    navController: NavController,
    arguments : String,
    clientViewModel : ClientViewModel = hiltViewModel(),
){
    val clientid = arguments.split("|").get(0)
    val distance = arguments.split("|").get(1)
    val client_ = clientViewModel.clients.value.data?.toList()?.filter { client ->
        client.uid.toString() == clientid
    }
    var client = M_Client()

    if (!client_?.isEmpty()!!) {
        client = client_.get(0)
    }
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val scrH = LocalConfiguration.current.screenHeightDp
    val scrW = LocalConfiguration.current.screenWidthDp
    val selectedTime = remember { mutableStateOf("Select a Time") }
    val selectedDate = remember { mutableStateOf("Select a date") }
    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.US) }
    val contactNumber = remember { mutableStateOf("") }
    val contactMail = remember { mutableStateOf("") }
    val showDateDialog = remember { mutableStateOf(false) }
    val showTimeDialog = remember { mutableStateOf(false) }
    val showCircular = remember { mutableStateOf(false) }
    val selectedState = remember { mutableStateOf(false) }
    Surface(modifier = Modifier.fillMaxSize()) {
        if (showDateDialog.value) {
            DatePickerDialog(
                onDismissRequest = { showDateDialog.value = false },
                onDateSelected = { date ->
                    selectedDate.value = dateFormatter.format(date)
                    showDateDialog.value = false
                },
                onOkPressed = {dates ->
                    selectedDate.value = dates
                    showDateDialog.value = false
                }
            )
        }
        if (showTimeDialog.value){
            TimePickerDialog(
                onDismissRequest = { showTimeDialog.value = false },
                onTimeSelected = { selectedTime.value = it }
            )
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
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally) {

                    Spacer(modifier = Modifier.height((scrH / 36.9).dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(0.95f),
                        horizontalArrangement = Arrangement.Start
                    ){
                        Text(
                            text = "Please Enter the your  Details To Book Appointment with \n ${client.doctorName}!!",
                            color = Color(0xFF1F4037),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.offset(x = screenWidth / 72)
                        )
                    }
                    Spacer(modifier = Modifier.height((scrH / 20.5).dp))
                    Card(modifier = Modifier
                        .fillMaxWidth()
                        .height((scrH / 3.88).dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF4F6F5)),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally ,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(5.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(0.95f),
                                horizontalArrangement = Arrangement.Start
                            ){
                                Text(text = "Contact Info" , color = Color(0xFF1F4037) , fontSize = 24.sp , fontWeight = FontWeight.Bold)
                            }
                            PhonenumberInput(onChange = {contactNumber.value= it})
                            EmailInput(onChange = {contactMail.value= it})
                        }
                    }

                    Spacer(modifier = Modifier.height(screenHeight / 41))
                    Card(modifier = Modifier
                        .fillMaxWidth()
                        .height(screenHeight / 7),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF4F6F5)),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(5.dp)
                        ){
                            Row(
                                modifier = Modifier.fillMaxWidth(0.95f),
                                horizontalArrangement = Arrangement.Start
                            ){
                                Text(text = "Select A Time Slot!!" , color = Color(0xFF1F4037) , fontSize = 24.sp , fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height((scrH / 92.25).dp))
                            Row(modifier = Modifier
                                .fillMaxWidth(0.95f)
                            ) {
                                Button(
                                    onClick = {
                                        showDateDialog.value = true
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth(0.5f)
                                        .padding(end = screenWidth / 72)
                                        .height((scrH / 16.4).dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1F4037) , contentColor = Color.White),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(text = selectedDate.value)
                                }

                                Button(
                                    onClick = {
                                        showTimeDialog.value = true
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = screenWidth / 72)
                                        .height((scrH / 16.4).dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1F4037) , contentColor = Color.White),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(text = selectedTime.value)
                                }
                            }
                        }
                    }
                    val lifecycleOwner = LocalLifecycleOwner.current
                    Spacer(modifier = Modifier.height((scrH / 21.08 ).dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height((scrH / 13.41).dp)
                            .clip(RoundedCornerShape(15.dp))
                            .clickable {
                                lifecycleOwner.lifecycleScope.launch {
                                    showCircular.value = true
                                    saveToAppointmentFirebase(
                                        M_Appointment(
                                            Clientid = client.uid,
                                            clinicName = client.clinicName,
                                            doctorName = client.doctorName,
                                            bookingId = UUID
                                                .randomUUID()
                                                .toString(),
                                            UsercontactNumber = contactNumber.value.toString(),
                                            Priority = "${selectedDate.value} , ${selectedTime.value}",
                                            BookingState = "Requested",
                                            UsercontactMail = contactMail.value.toString(),
                                            ImageUrl = client.imageUrl,
                                            ClientcontactMail = client.contactMail,
                                            Clientaddress = client.address,
                                            ClientcontactNumber = client.contactNumber
                                        )
                                    )
                                    delay(2000)
                                    showCircular.value = false
                                    selectedState.value = true
                                }
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
        if (showCircular.value){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Transparent.copy(alpha = 0.7f)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(50.dp)
                )
            }
        }
        if (selectedState.value){
            ConfirmingDialog (
                client = client,
                date = selectedDate.value,
                time = selectedTime.value
            ) {
                selectedState.value = false
                navController.popBackStack()
            }
        }
    }
}

@Composable
fun ConfirmingDialog(
    client: M_Client = M_Client(),
    date : String = "16/4/2024",
    time : String = "Morning",
    onDismissRequest: () -> Unit = {}
){
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val scrH = LocalConfiguration.current.screenHeightDp
    val scrW = LocalConfiguration.current.screenWidthDp
    AlertDialog(
        modifier = Modifier
            .fillMaxWidth()
            .height((scrH / 1.476).dp),
        onDismissRequest = { onDismissRequest.invoke() },
        containerColor = Color(0xFFD6E6DA),
        confirmButton = {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(modifier = Modifier.fillMaxWidth() , horizontalArrangement = Arrangement.Center) {
                    Text(
                        text = "Appointment Details",
                        fontSize = 20.sp
                    )
                }
                Spacer(modifier = Modifier.height((scrH / 73.8).dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Doctors Details",
                        fontSize = 20.sp
                    )
                }
                Card(
                    elevation = CardDefaults.cardElevation(3.dp)
                ) {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .height((scrH / 14.76).dp) , verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.fillMaxWidth(0.2f)) {
                            Image(
                                painter = rememberImagePainter(data = client.imageUrl.toString()),
                                contentDescription = "",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .height((scrH / 14.76).dp)
                                    .width((scrH / 14.76).dp)
                                    .clip(RoundedCornerShape(15.dp))
                            )
                        }
                        Spacer(modifier = Modifier.width((scrW / 36).dp))
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(text = client.doctorName.toString() , fontSize = 20.sp , maxLines = 1 , overflow = TextOverflow.Ellipsis)
                            Text(text = client.clinicName.toString() , maxLines = 1 , overflow = TextOverflow.Ellipsis)
                        }
                    }
                }
                Spacer(modifier = Modifier.height((scrH / 36.9).dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Slot Details",
                        fontSize = 20.sp
                    )
                }
                Card(elevation = CardDefaults.cardElevation(3.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                                .height((scrH / 14.76).dp) ,
                            verticalAlignment = Alignment.CenterVertically ,
                            horizontalArrangement = Arrangement.Center)
                        {
                            Text(text = "${time} of ${date}" , fontSize = 20.sp )
                        }
                }
                Spacer(modifier = Modifier.height((scrH / 36.9).dp))
                Row(modifier = Modifier.fillMaxWidth() , horizontalArrangement = Arrangement.Center) {
                    Text(
                        text = "Status : Requested",
                        fontSize = 20.sp
                    )
                }

                Spacer(modifier = Modifier.height((scrH / 36.9).dp))
                Text(
                    text = "you will be notified when client responds",
                    color = Color(0xFF862F2F),
                    fontSize = 15.sp
                    )
                Button(
                    onClick = {
                        onDismissRequest.invoke()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1F4037)),
                    modifier = Modifier.fillMaxWidth(0.8f),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(text = "Continue")
                }
            }
            
        }
    )
}

@Composable
fun EmailInput(
    contact_Mail : String = "",
    onChange: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val contactMail = remember { mutableStateOf(contact_Mail) }
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val scrH = LocalConfiguration.current.screenHeightDp
    val scrW = LocalConfiguration.current.screenWidthDp
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .padding(top = (scrH / 73.8).dp)
            .border(2.dp, color = Color(0xFF1F4037), shape = RoundedCornerShape(10.dp))
    ) {
        Box(
            modifier = Modifier
                .padding(
                    top = (scrH / 147.6).dp,
                    bottom = (scrH / 147.6).dp,
                    start = (scrW / 72).dp,
                    end = (scrW / 72).dp
                )
                .height((scrH / 13.41).dp)
        )
        {
            Text(text = "Contact Email", fontSize = 12.sp, color = Color(0xFF1F4037) , modifier = Modifier.offset(x = (scrW / 90).dp , y= -(scrH / 147.6).dp))
            BasicTextField(
                value = contactMail.value,
                onValueChange = {
                    contactMail.value = it
                    onChange(contactMail.value)
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        onChange(contactMail.value)
                    }
                ),
                maxLines = 1,
                textStyle = TextStyle(
                    MaterialTheme.colorScheme.outline,
                    fontSize = 19.sp,
                    textAlign = TextAlign.Justify
                ),
                modifier = Modifier
                    .padding(
                        top = (scrH / 147.6).dp,
                        bottom = (scrH / 147.6).dp,
                        start = (scrW / 72).dp,
                        end = (scrW / 72).dp
                    )
                    .align(Alignment.CenterStart)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun PhonenumberInput(
    contact_Number : String = "",
    onChange:(String) -> Unit = {}
){
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val scrH = LocalConfiguration.current.screenHeightDp
    val scrW = LocalConfiguration.current.screenWidthDp
    val keyboardController = LocalSoftwareKeyboardController.current
    val selectedCountryCode = remember { mutableStateOf("+91") }
    val contactNumber = remember { mutableStateOf(contact_Number) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .border(2.dp, color = Color(0xFF1F4037), shape = RoundedCornerShape(10.dp))
    ) {
        CountryCode_Dropdown(
            selectedCode = selectedCountryCode.value,
            onCodeSelected = { selectedCountryCode.value = it }
        )
        Spacer(modifier = Modifier.width((scrW / 90).dp))
        Box(
            modifier = Modifier
                .padding(
                    top = (scrH / 147.6).dp,
                    bottom = (scrH / 147.6).dp,
                    start = (scrW / 72).dp,
                    end = (scrW / 72).dp
                )
                .height((scrH / 13.41).dp)
        )
        {
            Text(text = "Phone Number", fontSize = 10.6.sp, color = Color(0xFF1F4037) , modifier = Modifier.offset(y = -(scrH / 147.6).dp , x = (scrW / 72).dp))
            BasicTextField(
                value = contactNumber.value,
                onValueChange = {
                    contactNumber.value = it
                    if (it.length == 10){
                        keyboardController?.hide()
                        onChange("${selectedCountryCode.value}${it}")
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                            onChange("${selectedCountryCode.value}${contactNumber.value}")
                    }
                ),
                maxLines = 1,
                textStyle = TextStyle(
                    MaterialTheme.colorScheme.outline,
                    fontSize = 23.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Justify,
                    letterSpacing = 0.17.em
                ),
                modifier = Modifier
                    .padding(
                        top = (scrH / 147.6).dp,
                        bottom = (scrH / 147.6).dp,
                        start = (scrW / 72).dp,
                        end = (scrW / 72).dp
                    )
                    .align(Alignment.CenterStart)
                    .fillMaxWidth()
            )
        }
    }
}


@Composable
fun CountryCode_Dropdown(
    selectedCode: String,
    onCodeSelected: (String) -> Unit
) {
    val expanded = remember { mutableStateOf(false) }
    val scrH = LocalConfiguration.current.screenHeightDp
    val scrW = LocalConfiguration.current.screenWidthDp
    Box(modifier = Modifier
        .width((scrW / 5.14).dp)
        .height((scrH / 13.41).dp)
        .padding(
            bottom = (scrH / 369).dp,
            start = (scrW / 180).dp,
            end = (scrW / 180).dp
        )
        .shadow(elevation = 0.dp, spotColor = Color(0xFF727972))
    ) {
        Text(
            text = "CountryCode" ,
            fontSize = 10.6.sp ,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = -(scrH / 147.6).dp),
            color = Color(0xFF1F4037)
        )
        Text(
            text = selectedCode,
            fontWeight = FontWeight.SemiBold,
            fontSize = 21.sp,
            modifier = Modifier
                .align(alignment = Alignment.CenterStart)
                .padding(
                    top = (scrH / 147.6).dp,
                    bottom = (scrH / 147.6).dp,
                    start = (scrW / 72).dp,
                    end = (scrW / 72).dp
                ),
            color = MaterialTheme.colorScheme.outline
        )
        IconButton(
            onClick = { expanded.value = true },
            modifier = Modifier
                .align(alignment = Alignment.CenterEnd)
                .clip(CircleShape)
                .size(30.dp)
        ) {
            Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Country Code")
        }
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
            modifier = Modifier.background(color = Color(0xFFD6E6DA))
        ) {
            countryCodes.forEach { (code, country) ->
                DropdownMenuItem(
                    text = { Text(text = "${code} ${country}")},
                    onClick = {
                        onCodeSelected(code)
                        expanded.value = false
                    }
                )
            }
        }
    }
}


@Composable
fun TimePickerDialog(
    onDismissRequest: () -> Unit,
    onTimeSelected: (String) -> Unit,
) {
    AlertDialog(
        onDismissRequest = { onDismissRequest.invoke()},
        confirmButton = {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Button(
                                onClick = {
                                onTimeSelected("Morning")
                                onDismissRequest.invoke()
                                          },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1F4037)),
                                modifier = Modifier.fillMaxWidth(0.8f),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(text = "Morning")
                            }
                            Button(
                                onClick = {
                                onTimeSelected("Afternoon")
                                onDismissRequest.invoke()
                                          },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1F4037)),
                                modifier = Modifier.fillMaxWidth(0.8f),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(text = "Afternoon")
                            }
                            Button(
                                onClick = {
                                onTimeSelected("Evening")
                                onDismissRequest.invoke()
                                          },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1F4037)),
                                modifier = Modifier.fillMaxWidth(0.8f),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(text = "Evening")
                            }
                        }
        },
        containerColor = Color(0xFFD6E6DA)

    )
}

@Composable
fun DatePickerDialog(
    onDismissRequest: () -> Unit,
    onDateSelected: (Date) -> Unit,
    onOkPressed : (String) -> Unit
) {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = "Select a date") },
        text = {
            AndroidView(
                factory = { context ->
                    DatePicker(context).apply {
                        val calendar = Calendar.getInstance()
                        init(
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        ) { _, year, month, dayOfMonth ->
                            val selectedCalendar = Calendar.getInstance().apply {
                                set(year, month, dayOfMonth)
                            }
                            val selectedDate = selectedCalendar.time.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                            if (!selectedDate.isBefore(LocalDate.now())) {
                                onDateSelected(selectedCalendar.time)
                            }
                        }
                    }
                }
            )
        },
        confirmButton = {
            Button(
                onClick = {
                onOkPressed(LocalDateTime.now().format(formatter))
                onDismissRequest.invoke()
                },
                colors = ButtonDefaults.buttonColors(Color(0xFF1F4037))
            ) {
                Text("OK")
            }
        },
        containerColor = Color(0xFFD6E6DA)
    )
}

fun saveToAppointmentFirebase(
    track: M_Appointment,
    onSuccess: () -> Unit = {}
) {
    val db = FirebaseFirestore.getInstance()
    val dbCollection = db.collection("appointments")
    val mClient = M_Appointment(
        Userid = FirebaseAuth.getInstance().currentUser?.uid.toString(),
        clinicName = track.clinicName,
        doctorName = track.doctorName,
        Clientid = track.Clientid,
        bookingId = track.bookingId,
        Useraddress = track.Useraddress,
        UsercontactNumber = track.UsercontactNumber,
        UsercontactMail = track.UsercontactMail,
        Priority = track.Priority,
        BookingState = track.BookingState,
        ImageUrl = track.ImageUrl,
        Clientaddress = track.Clientaddress,
        ClientcontactNumber = track.ClientcontactNumber,
        ClientcontactMail = track.ClientcontactMail
    )
    if (!mClient.toString().isNullOrEmpty()){
        dbCollection.add(mClient)
            .addOnSuccessListener { documentRef ->
                val docId = documentRef.id
                dbCollection.document(docId)
                    .update(hashMapOf( "docid" to docId ) as Map<String,Any>)
                    .addOnCompleteListener{task ->
                        if (task.isSuccessful){
                            Log.d("FirebaseSave" , "saveToAppointmentFirebase : ${task}")
                            onSuccess.invoke()
                        }
                    }
                    .addOnFailureListener{
                        Log.w("Error","SavetoAppointmentfirebase : " , it)
                    }
            }
    }
}