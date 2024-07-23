package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SettingActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        val backButton = findViewById<ImageView>(R.id.back_button)
        val shareButton = findViewById<TextView>(R.id.share_button)
        val helpButton = findViewById<TextView>(R.id.help_button)
        val userButton = findViewById<TextView>(R.id.user_button)


        backButton.setOnClickListener {
            super.onBackPressed()
        }

        shareButton.setOnClickListener {
            val shareMessage = getString(R.string.share_app)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)

           // startActivity(Intent.createChooser(shareIntent, R.string.share_app_header.toString()))
            startActivity(shareIntent)
        }
        helpButton.setOnClickListener{
            val helpIntent = Intent(Intent.ACTION_SENDTO)
            helpIntent.data = Uri.parse("mailto:")

            helpIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.mail)))
            helpIntent.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.helpTopic))
            helpIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.helpMessage))
            startActivity(helpIntent)

        }
        userButton.setOnClickListener{
            val url = Uri.parse(getString(R.string.userAgreement))
            val intent = Intent(Intent.ACTION_VIEW, url)
            startActivity(intent)
        }


    }
}