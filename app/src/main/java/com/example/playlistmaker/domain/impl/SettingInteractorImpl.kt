package com.example.playlistmaker.domain.interactor

import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.domain.api.SettingRepository

class SettingInteractorImpl(private val settingsRepository: SettingRepository) : SettingsInteractor {
    override var darkTheme: Boolean
        get() = settingsRepository.darkTheme
        set(value) {
            settingsRepository.switchDarkTheme(value)
        }

    override fun switchTheme(isDark: Boolean) {
        settingsRepository.switchDarkTheme(isDark)
        applyTheme(darkTheme)
    }
    private fun applyTheme(isDark: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isDark) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )

    }
}
