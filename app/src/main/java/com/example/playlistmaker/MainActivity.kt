package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val searchButton = findViewById<Button>(R.id.search_button)
        val mediaButton = findViewById<Button>(R.id.media_button)
        val configureButton = findViewById<Button>(R.id.configuration_button)

        val buttonClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity, "Идут ремонтные работы...", Toast.LENGTH_LONG).show()

            }
        }

        searchButton.setOnClickListener(buttonClickListener)

        mediaButton.setOnClickListener(){
            Toast.makeText(this@MainActivity, "Технические шоколадки!", Toast.LENGTH_SHORT).show()
        }

        configureButton.setOnClickListener(){
            Toast.makeText(this@MainActivity, "Пришельцы украли реализацию кнопки", Toast.LENGTH_LONG).show()
        }


    }
    //Hello
}