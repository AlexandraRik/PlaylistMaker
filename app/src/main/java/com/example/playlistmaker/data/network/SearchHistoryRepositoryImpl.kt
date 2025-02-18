package com.example.playlistmaker.data.network

import android.content.SharedPreferences
import com.example.playlistmaker.data.dto.TrackDTO
import com.example.playlistmaker.domain.api.SearchHistoryRepository
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistoryRepositoryImpl(private val sharedPreferences: SharedPreferences): SearchHistoryRepository {
    private val gson = Gson()
    private val trackType = object : TypeToken<MutableList<TrackDTO>>() {}.type

    override fun getHistory(): List<Track> {
        val json = sharedPreferences.getString(SEARCH_HISTORY_KEY, null)
        return if (json != null) {
            val historyDTO: List<TrackDTO> = gson.fromJson(json, trackType)
            historyDTO.map { it.toTrack() }
        } else {
            emptyList()
        }
    }

    override fun addTrack(track: Track) {
        val history = getHistory().toMutableList()
        history.removeAll { it.trackId == track.trackId }
        history.add(0, track)
        if (history.size > MAX_HISTORY_SIZE) {
            history.removeAt(MAX_HISTORY_SIZE)
        }
        saveHistory(history)
    }

    override fun clearHistory() {
        saveHistory(emptyList())
    }

    private fun saveHistory(history: List<Track>) {
        val historyDTOs = history.map { it.toSearchHistoryDTO() }
        val json = gson.toJson(historyDTOs)
        sharedPreferences.edit().putString(SEARCH_HISTORY_KEY, json).apply()
    }

    fun Track.toSearchHistoryDTO(): TrackDTO {
        return TrackDTO(
            trackName = this.trackName,
            artistName = this.artistName,
            trackTime = this.trackTime,
            artworkUrl100 = this.artworkUrl100,
            trackId = this.trackId,
            collectionName = this.collectionName,
            releaseDate = this.releaseDate,
            primaryGenreName = this.primaryGenreName,
            country = this.country,
            previewUrl = this.previewUrl
        )
    }

    fun TrackDTO.toTrack(): Track {
        return Track(
            trackName = this.trackName,
            artistName = this.artistName,
            trackTime = this.trackTime,
            artworkUrl100 = this.artworkUrl100,
            trackId = this.trackId,
            collectionName = this.collectionName,
            releaseDate = this.releaseDate,
            primaryGenreName = this.primaryGenreName,
            country = this.country,
            previewUrl = this.previewUrl
        )
    }
    companion object {
        private const val SEARCH_HISTORY_KEY = "search_history"
        private const val MAX_HISTORY_SIZE = 10
    }
}