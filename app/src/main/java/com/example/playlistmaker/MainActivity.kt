package com.example.playlistmaker

import android.content.Intent
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
               // Toast.makeText(this@MainActivity, "Идут ремонтные работы...", Toast.LENGTH_LONG).show() //реализация появления сообщения тост через анонимный класс
                val displayIntent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(displayIntent)
            }
        }

        searchButton.setOnClickListener(buttonClickListener)

        mediaButton.setOnClickListener(){
           // Toast.makeText(this@MainActivity, "Технические шоколадки!", Toast.LENGTH_SHORT).show()
            val displayIntent = Intent(this, MediaActivity::class.java)
            startActivity(displayIntent)
        }

        configureButton.setOnClickListener(){
           // Toast.makeText(this@MainActivity, "Пришельцы украли реализацию кнопки", Toast.LENGTH_LONG).show()
            val displayIntent = Intent(this, SettingActivity::class.java)
            startActivity(displayIntent)
        }


    }
    //Hello
}