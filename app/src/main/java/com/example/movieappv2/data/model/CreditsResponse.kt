package com.example.movieappv2.data.model

import com.google.gson.annotations.SerializedName

data class CreditsResponse(
    @SerializedName("cast") val cast: List<Cast>
)