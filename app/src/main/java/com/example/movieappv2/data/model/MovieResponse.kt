package com.example.movieappv2.data.model

import com.google.gson.annotations.SerializedName


// danh sách phim được lấy từ API
data class MovieResponse(
    @SerializedName("results") val movies: List<Movie>
)