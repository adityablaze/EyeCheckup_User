package com.example.eyecheckup_user

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class AudioPlayerViewModel : ViewModel() {
    var mediaPlayer: MediaPlayer? = null
    var _isPlaying = mutableStateOf(false)
    var currentPosition =  mutableStateOf(0f) // Track current position for slider
    var _duration = mutableStateOf(0f)// Track total duration of audio

    // Function to start playing the audio
    fun startPlaying(context: Context, audioResId: Int) {
        mediaPlayer?.release() // Release if an instance already exists
        mediaPlayer = MediaPlayer.create(context, audioResId).apply {
            setOnPreparedListener {
                _duration.value = it.duration.toFloat()// Set duration when media is ready
                it.start()
                _isPlaying.value = true
            }
        }
    }

    // Function to pause the audio
    fun pausePlaying() {
        mediaPlayer?.pause()
        _isPlaying.value = false
    }

    fun ContinuePlaying() {
        mediaPlayer?.start()
        _isPlaying.value = true
    }

    // Function to seek the audio to a specific position
    fun seekTo(position: Float) {
        mediaPlayer?.seekTo(position.toInt())
    }

    // Function to update the current position periodically
    fun updateCurrentPosition(
        position: (Float) -> Unit
    )  {
        mediaPlayer?.let {
            if (_isPlaying.value) {
                currentPosition.value = it.currentPosition.toFloat()
            }
            position(it.currentPosition.toFloat())
        }
    }

    // Release resources when ViewModel is destroyed
    override fun onCleared() {
        super.onCleared()
        mediaPlayer?.release()
    }
}