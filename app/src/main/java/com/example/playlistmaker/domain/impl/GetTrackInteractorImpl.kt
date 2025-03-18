package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.GetTrackInteractor
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.models.Track

class GetTrackInteractorImpl(private val trackRepository: TrackRepository): GetTrackInteractor {
    override fun getTrackById(track: Track): Track? {
        return trackRepository.getTrackById(track)
    }
    }