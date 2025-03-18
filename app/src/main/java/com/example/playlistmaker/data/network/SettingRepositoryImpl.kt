package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.preferences.SharedPreferencesManager
import com.example.playlistmaker.domain.api.SettingRepository


class SettingRepositoryImpl(private val preferencesManager: SharedPreferencesManager) :
    SettingRepository {
    override var darkTheme: Boolean
        get() = preferencesManager.darkTheme
        set(value) {
            preferencesManager.darkTheme = value
        }

    override fun switchDarkTheme(isDark: Boolean) {
        darkTheme = isDark
    }
}
