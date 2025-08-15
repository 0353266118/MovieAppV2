package com.example.movieappv2.data.model

import com.google.gson.annotations.SerializedName

data class ReviewResponse(
    @SerializedName("results") val reviews: List<Review>
)