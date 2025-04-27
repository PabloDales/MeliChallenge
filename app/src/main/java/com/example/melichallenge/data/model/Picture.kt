package com.example.melichallenge.data.model

import com.google.gson.annotations.SerializedName


data class Picture(
    @SerializedName("id") val id: String,
    @SerializedName("url") val url: String
)