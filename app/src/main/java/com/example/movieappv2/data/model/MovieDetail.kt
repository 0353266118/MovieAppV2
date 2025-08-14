package com.example.movieappv2.data.model

import com.google.gson.annotations.SerializedName

// chi tiết về đối tượng phim
data class MovieDetail(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("poster_path") val posterPath: String,
    @SerializedName("backdrop_path") val backdropPath: String, // Ảnh nền lớn
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("genres") val genres: List<Genre> // Danh sách thể loại
)