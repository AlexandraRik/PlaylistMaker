package com.example.playlistmaker

import android.content.SharedPreferences
import com.example.playlistmaker.data.network.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitClient
import com.example.playlistmaker.data.network.SearchHistory
import com.example.playlistmaker.data.network.SongRepositoryImpl
import com.example.playlistmaker.domain.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.api.SearchHistoryRepository
import com.example.playlistmaker.domain.api.SongInteractor
import com.example.playlistmaker.domain.api.SongRepository
import com.example.playlistmaker.domain.impl.SongInteractorImpl

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



}