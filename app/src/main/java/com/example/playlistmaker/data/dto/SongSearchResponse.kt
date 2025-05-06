package com.example.playlistmaker.data.dto

import com.example.playlistmaker.data.dto.SongDTO
import com.google.gson.annotations.SerializedName
data class SongSearchResponse(
    @SerializedName("resultCount")
    val resultCount: Int,
    @SerializedName("results")
    val results: List<SongDTO>
): Response()
