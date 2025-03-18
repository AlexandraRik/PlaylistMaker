package com.example.playlistmaker.domain.api

interface SettingRepository {
    var darkTheme: Boolean
    fun switchDarkTheme(isDark:Boolean)
}