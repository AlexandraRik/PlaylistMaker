package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate

class App: Application() {

    var darkTheme: Boolean = false
    private val sharedPrefs: SharedPreferences by lazy {
        getSharedPreferences("app_settings", MODE_PRIVATE)
    }

    override fun onCreate() {
        super.onCreate()
        if (sharedPrefs.contains("dark_theme")) {
            darkTheme = sharedPrefs.getBoolean("dark_theme", false)
        } else {
            darkTheme = (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
            sharedPrefs.edit().putBoolean("dark_theme", darkTheme).apply()
        }
        switchTheme(darkTheme)
    }
    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        sharedPrefs.edit().putBoolean("dark_theme", darkThemeEnabled).apply()

        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }


}

