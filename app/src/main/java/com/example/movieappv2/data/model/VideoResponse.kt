package com.example.movieappv2.data.model

import com.google.gson.annotations.SerializedName

data class VideoResponse(
    @SerializedName("results") val videos: List<Video>
)