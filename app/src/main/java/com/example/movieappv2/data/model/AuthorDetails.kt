package com.example.movieappv2.data.model

import com.google.gson.annotations.SerializedName

data class AuthorDetails(
    @SerializedName("rating") val rating: Double? // Rating có thể null
)