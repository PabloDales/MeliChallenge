package com.example.melichallenge.data.repository

import com.example.melichallenge.data.model.ProductDetail
import com.example.melichallenge.data.model.SearchResponse

interface ProductRepository {
    suspend fun searchProducts(
        query: String,
        offset: Int = 0
    ): Result<SearchResponse>


    suspend fun getProductById(id: String): ProductDetail

}