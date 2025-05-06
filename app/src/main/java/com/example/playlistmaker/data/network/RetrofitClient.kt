package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.SongSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient: NetworkClient {
    private val BASE_URL = "https://itunes.apple.com"

     val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    private val songService = retrofit.create(SongApiService::class.java)

    override fun doRequest(dto: Any): Response {
        if (dto is SongSearchRequest) {
            val resp = songService.search(dto.text).execute()

            val body = resp.body() ?: Response()

            return body.apply { resultCode = resp.code() }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }

    }
