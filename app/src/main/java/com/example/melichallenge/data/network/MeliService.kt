package com.example.melichallenge.data.network

import com.example.melichallenge.data.model.ProductDetail
import com.example.melichallenge.data.model.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MeliService {

    @GET("/products/search")
    suspend fun searchProducts(
        @Query("q") query: String,
        @Query("status") status: String? = "active",
        @Query("site_id") siteId: String? = "MLA",
        @Query("offset") offset: Int? = 0,
        @Query("limit") limit: Int? = 20
    ): SearchResponse


    @GET("/products/{id}")
    suspend fun getProductById(
        @Path("id") productId: String
    ): ProductDetail
}
