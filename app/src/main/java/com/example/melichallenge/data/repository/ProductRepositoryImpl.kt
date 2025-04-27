package com.example.melichallenge.data.repository

import com.example.melichallenge.data.datasource.RemoteDataSource
import com.example.melichallenge.data.model.ProductDetail
import com.example.melichallenge.data.model.SearchResponse
import javax.inject.Inject


class ProductRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : ProductRepository {
    override suspend fun searchProducts(query: String, offset: Int): Result<SearchResponse> {
        return remoteDataSource.searchProducts(query, offset = offset)
    }


    override suspend fun getProductById(id: String): ProductDetail {
        return remoteDataSource.getProductById(id)
    }


}