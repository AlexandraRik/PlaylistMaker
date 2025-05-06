package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.SongDTO
import com.example.playlistmaker.data.dto.SongSearchRequest
import com.example.playlistmaker.data.dto.SongSearchResponse
import com.example.playlistmaker.domain.api.SongRepository
import com.example.playlistmaker.domain.models.Song

class SongRepositoryImpl(private val networkClient: NetworkClient):SongRepository {
    override fun search(text: String): List<Song> {
        val response = networkClient.doRequest(SongSearchRequest(text))
        if (response.resultCode == 200) {
            return (response as SongSearchResponse).results.map {
                formatToSong(it) }
        } else {
            return emptyList()
        }
    }
    private fun formatToSong(songDto:SongDTO): Song{
        return Song(
            trackId = songDto.trackId,
            trackName = songDto.trackName,
            artistName = songDto.artistName,
            trackTimeMillis = songDto.trackTimeMillis,
            artworkUrl100 = songDto.artworkUrl100,
            collectionName = songDto.collectionName,
            releaseDate = songDto.releaseDate,
            primaryGenreName = songDto.primaryGenreName,
            country = songDto.country,
            previewUrl = songDto.previewUrl
        )
    }
}