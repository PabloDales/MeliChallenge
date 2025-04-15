package com.example.melichallenge.data.model

import com.google.gson.annotations.SerializedName

data class Settings(
    @SerializedName("listing_strategy") val listingStrategy: String,
    @SerializedName("exclusive") val exclusive: Boolean
)
