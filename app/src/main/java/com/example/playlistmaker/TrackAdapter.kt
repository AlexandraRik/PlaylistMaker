package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class TrackAdapter(private var trackList: List<Track>) :
    RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {


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

    }

    override fun getItemCount(): Int = trackList.size


    class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val trackName: TextView = itemView.findViewById(R.id.trackName)
        private val artistName: TextView = itemView.findViewById(R.id.artistName)
        private val trackTime: TextView = itemView.findViewById(R.id.trackTime)
        private val trackImage: ImageView = itemView.findViewById(R.id.trackImage)

        fun bind(track: Track) {
            trackName.text = track.trackName
            artistName.text = track.artistName
            trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime.toLong())
            Glide.with(itemView)
                .load(track.artworkUrl100)
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .transform(RoundedCorners(2))
                .into(trackImage)
        }
    }
}