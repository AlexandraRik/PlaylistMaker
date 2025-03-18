package com.example.playlistmaker.domain.interactor

interface SettingsInteractor {
    var darkTheme: Boolean
    fun switchTheme(isDark: Boolean)
}
