package com.example.melichallenge.data.model

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("keywords") val keywords: String,
    @SerializedName("paging") val paging: Paging,
    @SerializedName("results") val results: List<Product>
)
