package com.example.playlistmaker.domain.impl

import com.bumptech.glide.util.Executors
import com.example.playlistmaker.domain.api.SongInteractor
import com.example.playlistmaker.domain.api.SongRepository

class SongInteractorImpl(private val repository: SongRepository): SongInteractor {

    override fun search(text: String, consumer: SongInteractor.SongConsumer) {
        val thread = Thread {
            consumer.consume(repository.search(text))
        }
        thread.start()
    }

}