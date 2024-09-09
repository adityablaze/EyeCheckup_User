@file:Suppress("UNREACHABLE_CODE")

package com.example.eyecheckup_user.Screens.AstamatismScreen

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.eyecheckup_user.AudioPlayerViewModel
import com.example.eyecheckup_user.Components.CameraPermission
import com.example.eyecheckup_user.R
import com.example.eyecheckup_user.VideoPlayerViewModel
import com.google.android.exoplayer2.ui.PlayerView
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.logging.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AstagmatismViewScreen(
    navController: NavController
) {
   VideoPlayerScreen()
}


@Composable
fun AudioPlayerScreen(){
    val context = LocalContext.current
    val viewModel: AudioPlayerViewModel = viewModel()
    var isPlaying = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val handler = remember { Handler(Looper.getMainLooper()) }
    val currentPosition = remember { mutableStateOf( 0f) }
    // Start updating the current position when playing starts
    LaunchedEffect(Unit) {
        while (true) {
            viewModel.updateCurrentPosition {
                currentPosition.value = it
                android.util.Log.d("AudioPlayerViewModel", it.toString())
            }
            delay(100)
        }
        android.util.Log.d("AudioPlayerViewModel", "Current Position: ${currentPosition.value}")
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = if (isPlaying.value) "Playing Audio" else "Audio Paused")

        // Slider to show the audio progress and allow seeking
        Slider(
            value = currentPosition.value,
            valueRange = 0f..viewModel._duration.value,
            onValueChange = { newPosition ->
                viewModel.seekTo(newPosition)
                currentPosition.value = newPosition// Seek to the new position when slider is dragged
            },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        )
        Text(text = viewModel._duration.value.toString())
        Text(text = currentPosition.value.toString())
        Spacer(modifier = Modifier.height(20.dp))

        Row {
            Button(onClick = {
                if (!isPlaying.value) {
                    coroutineScope.launch {
                        viewModel.startPlaying(context, R.raw.aakhon) // Replace with your audio file
                        isPlaying.value = true
                    }
                }
            }) {
                Text(text = "Play")
            }
            Spacer(modifier = Modifier.width(20.dp))

            Button(onClick = {
                if (isPlaying.value) {
                    viewModel.pausePlaying()
                    isPlaying.value = false
                }
                else if (!isPlaying.value) {
                    viewModel.ContinuePlaying()
                    isPlaying.value = true
                }
            }) {
                if (isPlaying.value) {
                    Text(text = "Pause")
                }
                else if (!isPlaying.value) {
                    Text(text = "continue")
                }
            }
        }
    }
}

@Composable
fun VideoPlayerScreen() {
    val context = LocalContext.current
    val viewModel = remember { VideoPlayerViewModel() }
    val player = remember { mutableStateOf(viewModel.exoPlayer) }


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // PlayerView wrapped in AndroidView to show the video
        AndroidView(
            factory = { context ->
                PlayerView(context).apply {
                    viewModel.initializePlayer(context, this) // Pass PlayerView to the ViewModel
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16 / 9f) // Adjust aspect ratio if needed
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Button(onClick = { viewModel.play() }) {
                Text(text = "Play")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = { viewModel.pause() }) {
                Text(text = "Pause")
            }
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            viewModel.exoPlayer?.release()
        }
    }
}