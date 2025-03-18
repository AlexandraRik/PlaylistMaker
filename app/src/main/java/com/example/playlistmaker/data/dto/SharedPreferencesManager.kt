package com.example.playlistmaker.data.preferences

import android.content.SharedPreferences

class SharedPreferencesManager(private val sharedPreferences: SharedPreferences) {
    var darkTheme: Boolean
        get() = sharedPreferences.getBoolean("dark_theme", false)
        set(value) {
            sharedPreferences.edit().putBoolean("dark_theme", value).apply()
        }
}
