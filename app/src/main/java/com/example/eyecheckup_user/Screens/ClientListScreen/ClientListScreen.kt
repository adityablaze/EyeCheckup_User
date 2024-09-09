package com.example.eyecheckup_user.Screens.ClientListScreen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.eyecheckup_user.Components.AppBar
import com.example.eyecheckup_user.Location
import com.example.eyecheckup_user.Screens.LocationScreen.LocationHelper
import com.example.eyecheckup_user.model.M_Client
import com.example.eyecheckup_user.naviation.CheckupScreens

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientListScreen(
    locationHelper: LocationHelper,
    navController: NavController,
    locationRequest: () -> Unit,
    clientViewModel: ClientViewModel = hiltViewModel(),
    viewModel: DistanceViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
){
    val clients = clientViewModel.clients.value.data?.toList()
    val userLatitude = locationHelper.locationState.value?.latitude
    val userLongitude = locationHelper.locationState.value?.longitude
    val star = remember {
        mutableStateOf(false)
    }
    val loadinList: List<M_Client> = listOf(M_Client(),M_Client(),M_Client(),M_Client(),M_Client(),M_Client(),M_Client())

    val targets  = remember {
        mutableStateListOf<Location>()
    }
    var sources : List<Location> = emptyList()

    val response =  viewModel.response.observeAsState()

    if (userLatitude != null && userLongitude != null) {
        sources = listOf(Location(listOf(userLatitude.toDouble(), userLongitude.toDouble())))
    }
    if (!clients?.isEmpty()!!  && !sources.isEmpty()) {
        LaunchedEffect(Unit) {
            clients.forEach { client ->
                   targets.add(Location(listOf( client.latitude!!.toDouble(), client.longitude!!.toDouble())))
                   Log.d("addedTarget" , targets.toList().toString())
            }
            viewModel.fetchDistance(sources, targets)
        }
    }

    if (response.value != null  || (userLatitude == null && userLongitude == null)) {
        LaunchedEffect(Unit) {
            Log.d("respo", "${response.value}")
            clients.forEachIndexed { index, client ->
                client.distance =
                    response.value?.sources_to_targets?.get(0)?.get(index)?.distance
            }
            star.value = true
        }
    }


    val sortedByDistance = clients.sortedBy { it.distance }
    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
       ) {
        AppBar(title = "Available Doctors", backEnabled = false, imageVector = Icons.Default.ArrowBack){
            navController.navigate(CheckupScreens.LocationScreen.name)
        }
        if (star.value) {
            sortedByDistance.forEach { client ->
                HospitalCard(
                    client,
                    onClick = {
                     navController.navigate(CheckupScreens.DoctorDetailScreen.name + "/${client.uid.toString()}|${client.distance.toString()}")
                    }
                )
            }
        }else{
            loadinList.forEach { client_->
                EmptyCard()
            }
        }
    }
}


@Composable
fun EmptyCard(
) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .height(150.dp)
        .padding(8.dp)
    ) {
    }
}

@Composable
fun HospitalCard(
    client: M_Client,
    onClick: () -> Unit = {}
) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .height(150.dp)
        .padding(8.dp)
        .clickable { onClick.invoke() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFD6E6DA))
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxSize()
                .padding(13.dp)
        ) {
            Column(modifier = Modifier.fillMaxHeight()) {
                if (!client.imageUrl.isNullOrEmpty()) {
                    Image(
                        painter = rememberImagePainter(data = "${client.imageUrl.toString()}.jpg"),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(12.dp))
                            .size(70.dp)
                            .background(Color.Gray).border(width = 1.dp , color = Color(0xFF1F4037) , shape =  RoundedCornerShape(12.dp))
                    )
                } else {
                    Icon(
                        modifier = Modifier
                            .clip(shape = CircleShape)
                            .size(80.dp),
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = ""
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    if (!client.distance.toString().isNullOrEmpty()) {
                        Text(text = "Distance: ", fontSize = 10.sp)
                        Text(text = "${client.distance?.div(1000)}")
                        Text(text = " Km", fontSize = 10.sp)
                    }
                }

            }
            Column(modifier = Modifier.fillMaxWidth(0.67f)) {
                Text(
                    text = client.doctorName.toString(),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = client.clinicName.toString(),
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = client.address.toString(),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis, fontSize = 14.sp, lineHeight = 17.sp
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(imageVector = Icons.Rounded.Star, contentDescription = "" , tint = Color(0xFF1F4037))
                Text(text = client.rating.toString())
            }
        }
    }
}

