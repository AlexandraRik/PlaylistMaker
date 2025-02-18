package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Song

interface SongRepository {
    fun search(text: String):List<Song>
}