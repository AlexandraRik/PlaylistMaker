package com.example.playlistmaker

import android.os.Build
import android.os.Bundle
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
            updloadSong(track)
        }
    }
    private fun updloadSong(track: Track) {
        val trackName = findViewById<TextView>(R.id.trackName)
        val artistName = findViewById<TextView>(R.id.artistName)
        val albumName = findViewById<TextView>(R.id.albumName)
        val releaseDate = findViewById<TextView>(R.id.yearName)
        val genreName = findViewById<TextView>(R.id.genreName)
        val countryName = findViewById<TextView>(R.id.countryName)
        val trackTime = findViewById<TextView>(R.id.trackTime)
        val trackImage = findViewById<ImageView>(R.id.trackImage)
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



}