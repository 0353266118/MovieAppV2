package com.example.movieappv2.data.model

import com.google.gson.annotations.SerializedName

data class Video(
    @SerializedName("key") val key: String, // Key của video trên YouTube
    @SerializedName("site") val site: String,
    @SerializedName("type") val type: String // Ví dụ: "Trailer", "Teaser"
)