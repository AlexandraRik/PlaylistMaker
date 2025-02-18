package com.example.playlistmaker.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track

class TrackAdapter(private var trackList: List<Track>, val trackListener: TrackListener):
    RecyclerView.Adapter<TrackViewHolder>() {


fun updateTracks(newTracks: List<Track>) {
    trackList = newTracks
    notifyDataSetChanged()
}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = trackList[position]
        holder.bind(track)
        holder.itemView.setOnClickListener {
            trackListener.onTrackClick(track)
        }
    }

    override fun getItemCount(): Int = trackList.size

    fun interface    TrackListener {
        fun onTrackClick(track: Track)
    }
}