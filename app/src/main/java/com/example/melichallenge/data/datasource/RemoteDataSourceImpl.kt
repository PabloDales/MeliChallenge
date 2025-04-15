package com.example.melichallenge.data.datasource

import com.example.melichallenge.data.model.ProductDetail
import com.example.melichallenge.data.model.SearchResponse
import com.example.melichallenge.data.network.MeliService
import okhttp3.Call
import okhttp3.Callback
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val meliService: MeliService
) : RemoteDataSource {
    override suspend fun searchProducts(query: String, offset: Int): Result<SearchResponse> {
        return try {
            val response = meliService.searchProducts(query, offset = offset)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getProductById(id: String): ProductDetail {
        return meliService.getProductById(id)
    }
}