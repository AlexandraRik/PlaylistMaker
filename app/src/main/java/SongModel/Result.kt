package SongModel

import com.google.gson.annotations.SerializedName
data class Result(
    @SerializedName("resultCount")
    val resultCount: Int,
    @SerializedName("results")
    val results: List<Song>
)