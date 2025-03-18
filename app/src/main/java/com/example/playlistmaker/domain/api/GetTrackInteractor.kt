package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface GetTrackInteractor {
    fun getTrackById(track: Track): Track?
}