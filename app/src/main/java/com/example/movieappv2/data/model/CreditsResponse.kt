package com.example.movieappv2.data.model

import com.google.gson.annotations.SerializedName

// Danh sách diễn viên được trả về từ API
data class CreditsResponse(
    @SerializedName("cast") val cast: List<Cast>
)