package com.example.eyecheckup_user.Screens.LoginScreen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.eyecheckup_user.R
import com.example.eyecheckup_user.naviation.CheckupScreens
import kotlinx.coroutines.delay
import java.lang.reflect.Array.set

@Composable
fun LoginScreen(
    phoneAuthHelper: PhoneAuthHelper,
    navController: NavController
    ) {
    val phoneNumber = remember { mutableStateOf("") }
    val verificationCode = remember { mutableStateOf("") }
    val isCodeSent = remember { mutableStateOf(false) }
    val selectedCountryCode =  remember { mutableStateOf("+91") }  // Default to the first country code
    val codeStatus = remember { mutableStateOf("") }
    val message = remember { mutableStateOf("") }
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val shift = remember{
        Animatable(0f)
    }
    LaunchedEffect(key1 = true){
        shift.animateTo(
            targetValue = -250f,
            animationSpec = tween(durationMillis = 800)
        )
    }
    LaunchedEffect(Unit){
        while (true) {
            phoneAuthHelper.codeStatus { codeStatus.value = it }
            delay(500)
        }
    }
    Box(modifier = Modifier.fillMaxSize())
    {
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
                    .offset(y = -180.dp)
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
                    drawPath(path = path, color = Color(0xFF99E197))
                }
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
            if (!isCodeSent.value) {
                LoginContent(selectedCountryCode, phoneNumber, phoneAuthHelper, isCodeSent)

            } else {
                VerificationContent(
                    verificationCode,
                    message,
                    phoneAuthHelper,
                    navController,
                    selectedCountryCode,
                    phoneNumber,
                    isCodeSent,
                    codeStatus
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "",
                modifier = Modifier
                    .size(220.dp)
                    .offset(y = shift.value.dp)
            )
        }
    }
}

@Composable
private fun VerificationContent(
    verificationCode: MutableState<String>,
    message: MutableState<String>,
    phoneAuthHelper: PhoneAuthHelper,
    navController: NavController,
    selectedCountryCode: MutableState<String>,
    phoneNumber: MutableState<String>,
    isCodeSent: MutableState<Boolean>,
    codeStatus: MutableState<String>
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val scrH = LocalConfiguration.current.screenHeightDp
    val scrW = LocalConfiguration.current.screenWidthDp
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
    Spacer(modifier = Modifier.height(200.dp))
    Text(
        text = "Please enter the One-Time Password sent to ${selectedCountryCode.value}*******${phoneNumber.value.takeLast(3)} to verify your account",
        fontSize = 16.sp,
        color = Color.White,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height((scrH / 20.5 ).dp))
    Spacer(modifier = Modifier.height(shift.value.dp))
    OtpInput(onDone = { code ->
        verificationCode.value = code
    })
    Spacer(modifier = Modifier.height((scrH / 369).dp))
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(
            top = (scrH / 73.8).dp,
            bottom = (scrH / 73.8).dp,
            start = (scrW / 36).dp,
            end = (scrW / 36).dp
        )) {
        Text(text = message.value , color = Color(0xFFF0606B) )
    }
    Button(
        onClick = {
            phoneAuthHelper.verifyPhoneNumberWithCode(
                verificationCode.value,
                success = { navController.navigate(CheckupScreens.HomeScreen.name){
                    popUpTo(CheckupScreens.LoginScreen.name){
                        inclusive = true
                    }
                } },
                onfailure = { message.value = it },
                )
                  },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF99E197),
            contentColor = Color.White
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height((scrH / 14.76).dp),
        shape = RoundedCornerShape(5.dp),
        enabled = codeStatus.value.isNotEmpty() && verificationCode.value.length == 6
    ) {
        Text("Validate")
    }
    Spacer(modifier = Modifier.height((scrH / 33.54).dp))
    Text(
        text = "Resend One-Time Password",
        modifier = Modifier.clickable {
            phoneAuthHelper.resendVerificationCode("${selectedCountryCode.value}${phoneNumber.value}")
        },
        color = Color(0xff646464).copy(alpha = transparency.value)
    )
    Spacer(modifier = Modifier.height((scrH / 49.2).dp))
    Text(
        text = "Entered a wrong number?",
        modifier = Modifier.clickable {
            isCodeSent.value = !isCodeSent.value
            codeStatus.value =  ""
                                      },
        color = Color(0xff646464).copy(alpha = transparency.value)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoginContent(
    selectedCountryCode: MutableState<String>,
    phoneNumber: MutableState<String>,
    phoneAuthHelper: PhoneAuthHelper,
    isCodeSent: MutableState<Boolean>
) {
    val scrH = LocalConfiguration.current.screenHeightDp
    val scrW = LocalConfiguration.current.screenWidthDp
    val keyboardController = LocalSoftwareKeyboardController.current

    Spacer(modifier = Modifier.height(200.dp))
    Row(modifier = Modifier.fillMaxWidth(0.95f)) {
        Text(
            text = "Enter your mobile number",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 23.sp
        )
    }
    Spacer(modifier = Modifier.height((scrH / 36.9).dp))
    Row(modifier = Modifier.fillMaxWidth(0.95f)) {
        Text(
            text = "Please confirm your country code and",
            color = Color.White,
            fontSize = 13.sp,
            lineHeight = 13.sp
        )
    }
    Row(modifier = Modifier.fillMaxWidth(0.95f)) {
        Text(
            text = "enter your phone number",
            color = Color.White,
            fontSize = 13.sp,
            lineHeight = 13.sp
        )
    }
    Spacer(modifier = Modifier.height(100.dp))
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(0.95f)
    ) {
        CountryCodeDropdown(
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
                value = phoneNumber.value,
                onValueChange = {
                    phoneNumber.value = it
                    if (it.length == 10){
                        keyboardController?.hide()
                    }
                                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
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
            )
            Spacer(
                modifier = Modifier
                    .height((scrH / 369).dp)
                    .fillMaxWidth()
                    .background(color = Color(0xFF1F4037))
                    .align(Alignment.BottomCenter)
            )
        }
    }

    Spacer(modifier = Modifier.height((scrH / 20.5).dp))
    Button(
        onClick = {
            phoneAuthHelper.startPhoneNumberVerification("${selectedCountryCode.value}${phoneNumber.value}")
            isCodeSent.value = true
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF99E197),
            contentColor = Color.White
        ),
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .height((scrH / 14.76).dp),
        shape = RoundedCornerShape(5.dp),
        enabled = phoneNumber.value.isNotEmpty()
    ) {
        Text("Send Code")
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun OtpInput(
    onDone: (String) -> Unit
) {
    val hell = remember {
        mutableStateOf("")
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    Box(modifier = Modifier.width(300.dp)) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            OutlinedTextField(value = "", onValueChange = {}, modifier = Modifier
                .padding(2.dp)
                .weight(1f), enabled = false)
            OutlinedTextField(value = "", onValueChange = {}, modifier = Modifier
                .padding(2.dp)
                .weight(1f), enabled = false)
            OutlinedTextField(value = "", onValueChange = {}, modifier = Modifier
                .padding(2.dp)
                .weight(1f), enabled = false)
            OutlinedTextField(value = "", onValueChange = {}, modifier = Modifier
                .padding(2.dp)
                .weight(1f), enabled = false)
            OutlinedTextField(value = "", onValueChange = {}, modifier = Modifier
                .padding(2.dp)
                .weight(1f), enabled = false)
            OutlinedTextField(value = "", onValueChange = {}, modifier = Modifier
                .padding(2.dp)
                .weight(1f), enabled = false)
        }
        BasicTextField(
            value = hell.value,
            onValueChange = {
                if (it.length <= 6) {
                    hell.value = it
                    if (it.length == 6) {
                        onDone(hell.value)
                        keyboardController?.hide()
                    }
                }
                            },
            modifier = Modifier
                .background(color = Color.Transparent)
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(1.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            maxLines = 1,
            textStyle = TextStyle(
                MaterialTheme.colorScheme.outline,
                fontSize = 23.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Justify,
                letterSpacing = 36.5.sp
            ),
            cursorBrush = SolidColor(Color.Transparent)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerificationCodeInput(
    onDone: (String) -> Unit
) {
    val codes = remember { mutableStateOf(List(6) { "" }) }
    val focusRequesters = List(6) { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        codes.value.forEachIndexed { index, code ->
            OutlinedTextField(
                value = code,
                onValueChange = { newCode ->
                    if (newCode.length <= 1) {
                        val newCodes = codes.value.toMutableList()
                        newCodes[index] = newCode
                        codes.value = newCodes

                        when {
                            newCode.isNotEmpty() && index < 5 -> {
                                focusRequesters[index + 1].requestFocus()
                            }
                            newCode.isEmpty() && index > 0 -> {
                                focusRequesters[index - 1].requestFocus()
                            }
                            index == 5 -> {
                                focusManager.clearFocus() // Optionally clear focus from the last field
                            }
                        }
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequesters[index]),
                maxLines = 1,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = if (index == 5) {
                        onDone(codes.value.joinToString(""))
                        ImeAction.Done
                    } else {
                        ImeAction.Next
                    }
                ),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF56CEC7),
                    unfocusedBorderColor = Color(0xFF56CEC7)),
                keyboardActions = KeyboardActions(
                    onNext = {
                        if (index < 5) {
                            focusRequesters[index + 1].requestFocus()
                        }
                    },
                    onDone = {
                        focusManager.clearFocus()
                        onDone(codes.value.joinToString(""))
                    }
                ),
                visualTransformation = SplitVisualTransformation(index),
                textStyle = TextStyle(fontSize = 20.sp)

            )
        }
    }
}


// Optionally add visual transformation to center the text
class SplitVisualTransformation(private val index: Int) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val transformedText = if (text.isEmpty()) {
            "•"
        } else {
            text.text
        }
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int = 1
            override fun transformedToOriginal(offset: Int): Int = 0
        }
        return TransformedText(AnnotatedString(transformedText), offsetMapping)
    }
}

val countryCodes = listOf(
    "+1" to "United States",
    "+91" to "India",
    "+44" to "United Kingdom",
    "+61" to "Australia",
    "+81" to "Japan",
    // Add more country codes as needed
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryCodeDropdown(
    selectedCode: String,
    onCodeSelected: (String) -> Unit
) {
    val scrH = LocalConfiguration.current.screenHeightDp
    val scrW = LocalConfiguration.current.screenWidthDp
    val expanded = remember { mutableStateOf(false) }

    Box(modifier = Modifier
        .width((scrW / 5.14).dp)
        .height((scrH / 12.5).dp)
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
                .offset(y = -(scrH / 199.6).dp),
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
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height((scrH / 369).dp)
            .background(color = Color(0xFF1F4037))
            .align(Alignment.BottomCenter)
            .offset(y = 5.dp)
        )
    }
}

