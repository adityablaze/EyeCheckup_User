package com.example.eyecheckup_user

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView

class VideoPlayerViewModel : ViewModel() {

    var exoPlayer: ExoPlayer? = null
        private set

    fun initializePlayer(context: Context, playerView: PlayerView) {
        if (exoPlayer == null) {
            // Load video from the res/raw folder
            val videoUri = Uri.parse("android.resource://${context.packageName}/${R.raw.beti}")

            exoPlayer = ExoPlayer.Builder(context).build().apply {
                val mediaItem = MediaItem.fromUri(videoUri)
                setMediaItem(mediaItem)
                prepare()

                // Bind the player to the PlayerView
                playerView.player = this
            }
        }
    }

    fun play() {
        exoPlayer?.playWhenReady = true
    }

    fun pause() {
        exoPlayer?.playWhenReady = false
    }

    override fun onCleared() {
        super.onCleared()
        exoPlayer?.release()
        exoPlayer = null
    }
}