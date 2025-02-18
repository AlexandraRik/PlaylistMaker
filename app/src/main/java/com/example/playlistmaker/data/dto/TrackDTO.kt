package com.example.playlistmaker.data.dto

import java.io.Serializable

public final data class TrackDTO(
    val trackName: String,// Название композиции
    val artistName: String,// Имя исполнителя
    val trackTime: String,// Продолжительность трека
    val artworkUrl100: String,// Ссылка на изображение обложки
    val trackId: Long,// Идентификатор для трека
    val collectionName: String?,// Название альбома (collectionName) (если есть)
    val releaseDate: String,// Год релиза трека (releaseDate)
    val primaryGenreName: String,// Жанр трека (primaryGenreName)
    val country: String, // Страна исполнителя (country)
    val previewUrl: String //Воспроизведение превью трека
): Serializable{

}



