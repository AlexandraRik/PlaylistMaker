package com.example.playlistmaker

import SongModel.Result
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface SongApiService {
    @GET("search?entity=song")
    fun search(@Query("term") text: String): Call<Result>
}