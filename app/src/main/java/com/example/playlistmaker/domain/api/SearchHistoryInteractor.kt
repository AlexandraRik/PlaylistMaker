package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface SearchHistoryInteractor {
    fun getHistory(consumer: HistoryConsumer)
    fun addTrack(track: Track)
    fun clearHistory()

    interface HistoryConsumer {
        fun consume(history: List<Track>)
    }
}