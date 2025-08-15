package com.example.movieappv2.data.model

import com.google.gson.annotations.SerializedName

data class Review(
    @SerializedName("author") val author: String,
    @SerializedName("content") val content: String,
    @SerializedName("author_details") val authorDetails: AuthorDetails,
    @SerializedName("created_at") val createdAt: String
)