package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Song

interface SongInteractor {
    fun search(text: String, consumer: SongConsumer)

    interface SongConsumer {
        fun consume(foundSong: List<Song>)
    }
}