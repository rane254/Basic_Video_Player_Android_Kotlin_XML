package com.coursera.video_player

import android.os.Bundle
import android.widget.VideoView
import android.widget.MediaController
import android.net.Uri
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.coursera.video_player.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var videoView: VideoView
    private lateinit var prevBtn: Button
    private lateinit var nextBtn: Button
    private var currentVideoIndex: Int = 0

    private val videoList: List<Uri> by lazy {
        listOf(
            Uri.parse("android.resource://${packageName}/raw/test1"),
            Uri.parse("android.resource://${packageName}/raw/test2")
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        videoView = binding.testVideoView
        prevBtn = binding.prevBtn
        nextBtn = binding.nextBtn

        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)

        videoView.setOnErrorListener { _, what, extra ->
            Log.e("VideoPlayer", "Error: $what, $extra")
            Toast.makeText(this, "Video playback error occurred", Toast.LENGTH_SHORT).show()
            true
        }

        if (savedInstanceState != null) {
            currentVideoIndex = savedInstanceState.getInt("currentVideoIndex", 0)
        }

        playVideoAtIndex(currentVideoIndex)
        binding.prevBtn.setOnClickListener {
            playPrevVideo()
        }

        binding.nextBtn.setOnClickListener {
            playNextVideo()
        }
    }

    private fun playVideoAtIndex(index: Int) {
        if (videoList.isEmpty()) {
            Log.e("VideoPlayer", "No videos available in the list.")
            return
        }
        val uri = videoList[index]
        videoView.setVideoURI(uri)
        videoView.requestFocus()
        videoView.start()
    }

    private fun playNextVideo() {
        currentVideoIndex = (currentVideoIndex + 1) % videoList.size
        playVideoAtIndex(currentVideoIndex)
    }

    private fun playPrevVideo() {
        currentVideoIndex = (currentVideoIndex - 1 + videoList.size) % videoList.size
        playVideoAtIndex(currentVideoIndex)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("currentVideoIndex", currentVideoIndex)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentVideoIndex = savedInstanceState.getInt("currentVideoIndex", 0)
        playVideoAtIndex(currentVideoIndex)
    }
}
