package com.example.movieappv2.data.model

import com.google.gson.annotations.SerializedName

data class Cast(
    @SerializedName("name") val name: String,
    @SerializedName("profile_path") val profilePath: String? // Đường dẫn có thể null
)