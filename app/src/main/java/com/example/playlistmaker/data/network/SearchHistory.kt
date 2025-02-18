package com.example.playlistmaker.data.network

import android.content.SharedPreferences
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class SearchHistory(private val sharedPreferences: SharedPreferences) {
    private val gson = Gson()
    private val trackType = object : TypeToken<MutableList<Track>>() {}.type


    fun getHistory(): MutableList<Track> {
        val json = sharedPreferences.getString(SEARCH_HISTORY_KEY, null)
        return if (json != null) {
            gson.fromJson(json, trackType)
        } else {
            mutableListOf()
        }
    }
    fun addTrack(track: Track) {
        val history = getHistory()
        history.removeAll { it.trackId == track.trackId }
        history.add(0, track)
        if (history.size > MAX_HISTORY_SIZE) {
            history.removeAt(MAX_HISTORY_SIZE)
        }
        saveHistory(history)
    }

    fun clearHistory() {
        saveHistory(mutableListOf())
    }

    private fun saveHistory(history: List<Track>) {
        val json = gson.toJson(history)
        sharedPreferences.edit().putString(SEARCH_HISTORY_KEY, json).apply()
    }


    companion object {
        private const val SEARCH_HISTORY_KEY = "search_history"
        private const val MAX_HISTORY_SIZE = 10
    }
}