package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.api.SearchHistoryRepository
import com.example.playlistmaker.domain.models.Track

class SearchHistoryInteractorImpl(private val historyRepository: SearchHistoryRepository):
    SearchHistoryInteractor {

    override fun getHistory(consumer: SearchHistoryInteractor.HistoryConsumer) {
        val thread = Thread {
            val history = historyRepository.getHistory()
            consumer.consume(history)
        }
        thread.start()
    }

    override fun addTrack(track: Track) {
        historyRepository.addTrack(track)
    }

    override fun clearHistory() {
        historyRepository.clearHistory()
    }
}