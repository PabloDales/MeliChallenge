package com.example.melichallenge.repository

import com.example.melichallenge.data.model.Paging
import com.example.melichallenge.data.model.Product
import com.example.melichallenge.data.model.ProductDetail
import com.example.melichallenge.data.model.SearchResponse
import com.example.melichallenge.data.model.TestDataFactory
import com.example.melichallenge.data.repository.ProductRepository

class FakeProductRepository : ProductRepository {
    private val products = mutableListOf<Product>()

    init {
        products.addAll(TestDataFactory.sampleProducts)
    }

    override suspend fun searchProducts(query: String, offset: Int): Result<SearchResponse> {

        val filteredProducts = products.filter {
            it.name.contains(query, ignoreCase = true) ||
                    (it.description?.contains(query, ignoreCase = true) ?: false) ||
                    (it.keywords?.contains(query, ignoreCase = true) ?: false)
        }

        val paginatedProducts = filteredProducts
            .drop(offset)
            .take(20)

        val searchResponse = SearchResponse(
            keywords = query,
            results = paginatedProducts,
            paging = Paging(
                total = filteredProducts.size,
                offset = offset,
                limit = 20
            )
        )

        return Result.success(searchResponse)
    }

    override suspend fun getProductById(id: String): ProductDetail {
        TODO("Not yet implemented")
    }

}