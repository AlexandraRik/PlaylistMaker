package com.example.playlistmaker

import android.content.SharedPreferences
import com.example.playlistmaker.data.network.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitClient
import com.example.playlistmaker.data.network.SongRepositoryImpl
import com.example.playlistmaker.data.preferences.SharedPreferencesManager
import com.example.playlistmaker.data.repository.SettingRepositoryImpl
import com.example.playlistmaker.domain.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.api.SearchHistoryRepository
import com.example.playlistmaker.domain.api.SettingRepository
import com.example.playlistmaker.domain.api.SongInteractor
import com.example.playlistmaker.domain.api.SongRepository
import com.example.playlistmaker.domain.impl.SongInteractorImpl
import com.example.playlistmaker.domain.interactor.SettingInteractorImpl
import com.example.playlistmaker.domain.interactor.SettingsInteractor

object Creator {
    private fun getSongRepository(): SongRepository {
        return SongRepositoryImpl(RetrofitClient)
    }

    private fun getHistoryRepository(sharedPreferences: SharedPreferences): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(sharedPreferences)
    }

    fun provideSongsInteractor(): SongInteractor {
        return SongInteractorImpl(getSongRepository())
    }

    fun provideHistoryInteractor(sharedPreferences: SharedPreferences): SearchHistoryInteractorImpl {
        val historyRepository = getHistoryRepository(sharedPreferences)
        return SearchHistoryInteractorImpl(historyRepository)
    }


    fun provideSharedPreferencesManager(app: App): SharedPreferencesManager {
        return SharedPreferencesManager(app.sharedPrefs)
    }

    fun provideSettingsRepository(sharedPreferencesManager: SharedPreferencesManager): SettingRepository {
        return SettingRepositoryImpl(sharedPreferencesManager)
    }

    fun provideSettingsInteractor(app: App): SettingsInteractor {
        val preferencesManager = provideSharedPreferencesManager(app)
        val settingsRepository = provideSettingsRepository(preferencesManager)
        return SettingInteractorImpl(settingsRepository)
    }




}