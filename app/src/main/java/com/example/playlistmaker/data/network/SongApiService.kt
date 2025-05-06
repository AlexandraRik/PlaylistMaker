package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.SongSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface SongApiService {
    @GET("search?entity=song")
    fun search(@Query("term") text: String): Call<SongSearchResponse>
}