package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.TrackDTO
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.models.Track

class TrackRepositoryImpl : TrackRepository {
    private val tracks = mutableListOf<TrackDTO>()

    override fun getTrackById(track: Track): Track? {
        val trackDTO = tracks.find { it.trackId.toString() == track.trackId.toString() }
        return trackDTO?.toDomain()
    }


    private fun Track.toDTO(): TrackDTO {
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

    private fun TrackDTO.toDomain(): Track {
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
}
