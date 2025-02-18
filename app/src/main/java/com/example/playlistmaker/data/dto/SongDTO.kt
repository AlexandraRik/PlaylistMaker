package com.example.playlistmaker.data.dto

import com.google.gson.annotations.SerializedName

data class SongDTO(
    @SerializedName("artistName")
    val artistName: String,
    @SerializedName("artworkUrl100")
    val artworkUrl100: String,
    @SerializedName("trackName")
    val trackName: String,
    @SerializedName("trackTimeMillis")
    val trackTimeMillis: String,
    @SerializedName("trackId")
    val trackId: Long,
    @SerializedName("collectionName")
    val collectionName: String?,
    @SerializedName("releaseDate")
    val releaseDate: String,
    @SerializedName("primaryGenreName")
    val primaryGenreName: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("previewUrl")
    val previewUrl: String
)


