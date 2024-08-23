package com.example.playlistmaker

import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class PlayerActivity: AppCompatActivity() {
    private var playerState = STATE_DEFAULT
    private lateinit var previewUrl: String
    private var mediaPlayer = MediaPlayer()
    private lateinit var playImage: ImageView
    private lateinit var stopImage: ImageView
    private lateinit var handler: Handler
    private lateinit var songTime: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.player_activity)
        val backButton = findViewById<ImageView>(R.id.back_button)
        backButton.setOnClickListener {
            super.onBackPressed()
        }

        val track: Track? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("track", Track::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("track") as? Track
        }
        if (track != null) {
            uploadSong(track)
        }
        songTime = findViewById<TextView>(R.id.songTime)
        handler = Handler(Looper.getMainLooper());
        preparePlayer()
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
        handler.removeCallbacks(updateTimer())
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler.removeCallbacks(updateTimer())
    }

    private fun uploadSong(track: Track) {
        val trackName = findViewById<TextView>(R.id.trackName)
        val artistName = findViewById<TextView>(R.id.artistName)
        val albumName = findViewById<TextView>(R.id.albumName)
        val releaseDate = findViewById<TextView>(R.id.yearName)
        val genreName = findViewById<TextView>(R.id.genreName)
        val countryName = findViewById<TextView>(R.id.countryName)
        val trackTime = findViewById<TextView>(R.id.trackTime)
        val trackImage = findViewById<ImageView>(R.id.trackImage)
         playImage = findViewById<ImageView>(R.id.play_icon)
         stopImage = findViewById<ImageView>(R.id.stop_icon)
        if (track.collectionName != null) {
            albumName.text = track.collectionName
            albumName.visibility = View.VISIBLE
            findViewById<TextView>(R.id.album).visibility = View.VISIBLE
        }
        trackName.text = track.trackName
        artistName.text = track.artistName
        trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime.toLong())
        releaseDate.text = getYear(track.releaseDate)
       genreName.text = track.primaryGenreName
        countryName.text = track.country
        previewUrl = track.previewUrl

        playImage.setOnClickListener {
            playbackControl()
        }


        Glide.with(this)
            .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder_huge_day)
            .centerCrop()
            .transform(RoundedCorners(20))
            .into(trackImage)

    }

    fun getYear(date:String):String{
        val formatter = DateTimeFormatter.ISO_DATE_TIME
        val localDate = LocalDate.parse(date, formatter)
        val year = localDate.year.toString()
        return year
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playImage.isEnabled = true
            playerState = STATE_PREPARED
            handler.removeCallbacks(updateTimer())
        }
        mediaPlayer.setOnCompletionListener {
            playImage.setImageResource(R.drawable.play)
            playerState = STATE_PREPARED
            handler.removeCallbacks(updateTimer())
            songTime.text = "00:00"
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playImage.setImageResource(R.drawable.pause)
        playerState = STATE_PLAYING
        handler.post(updateTimer())
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playImage.setImageResource(R.drawable.play)
        playerState = STATE_PAUSED
        handler.removeCallbacks(updateTimer())
    }

    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun updateTimer(): Runnable {
        return object : Runnable {
            override fun run() {
                if(playerState == STATE_PLAYING) {
                    val currentPosition = mediaPlayer.currentPosition
                    songTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(currentPosition)
                    handler.postDelayed(this, TIME_DELAY)
                }

            }
        }
    }
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val TIME_DELAY = 300L
    }
}


