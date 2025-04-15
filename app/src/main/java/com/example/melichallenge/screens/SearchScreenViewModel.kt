package com.example.melichallenge.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melichallenge.data.model.Product
import com.example.melichallenge.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val productsRepository: ProductRepository
) : ViewModel() {

    sealed interface SearchResultUiState {
        data object Loading : SearchResultUiState
        data object EmptyQuery : SearchResultUiState
        data object LoadFailed : SearchResultUiState
        data class Success(
            val products: List<Product> = emptyList(),
        ) : SearchResultUiState {
            fun isEmpty(): Boolean = products.isEmpty()
        }

        data object SearchNotReady : SearchResultUiState
    }

    data class SearchScreenUIState(
        val searchQuery: String = "",
        val searchResultUiState: SearchResultUiState = SearchResultUiState.EmptyQuery,
        val searchHistory: List<String> = emptyList(),
        val selectedProduct: Product? = null,
        val isLoadingMore: Boolean = false,
        val hasMoreProducts: Boolean = true,
        val currentOffset: Int = 0
    )

    private val _uiState = MutableStateFlow(SearchScreenUIState())
    val uiState: StateFlow<SearchScreenUIState> = _uiState.asStateFlow()

    fun updateSearchQuery(query: String) {
        _uiState.update {
            it.copy(
                searchQuery = query,
                searchResultUiState = if (query.isBlank()) SearchResultUiState.EmptyQuery else it.searchResultUiState
            )
        }
    }

    fun selectProduct(product: Product) {
        _uiState.update { it.copy(selectedProduct = product) }
    }

    fun onSearchTriggered(query: String) {
        if (query.isBlank()) {
            _uiState.update { it.copy(searchResultUiState = SearchResultUiState.EmptyQuery) }
            return
        }

        _uiState.update {
            it.copy(
                currentOffset = 0,
                hasMoreProducts = true
            )
        }

        viewModelScope.launch {
            try {
                _uiState.update { it.copy(searchResultUiState = SearchResultUiState.Loading) }

                productsRepository.searchProducts(
                    query = query,
                    offset = 0
                ).fold(
                    onSuccess = { searchResponse ->
                        val updatedHistory = _uiState.value.searchHistory.toMutableList()

                        if (query !in updatedHistory) {
                            updatedHistory.add(0, query)
                            if (updatedHistory.size > 10) {
                                updatedHistory.removeAt(updatedHistory.size - 1)
                            }
                        }

                        val total = searchResponse.paging.total
                        val hasMore = searchResponse.results.size >= 20 ||
                                (searchResponse.paging.total > searchResponse.results.size)

                        Log.d(
                            "Pagination",
                            "Search results: ${searchResponse.results.size}, Total: $total, HasMore: $hasMore"
                        )

                        _uiState.update {
                            it.copy(
                                searchResultUiState = SearchResultUiState.Success(searchResponse.results),
                                searchHistory = updatedHistory,
                                hasMoreProducts = hasMore,
                                currentOffset = searchResponse.results.size
                            )
                        }
                    },
                    onFailure = {
                        _uiState.update {
                            it.copy(searchResultUiState = SearchResultUiState.LoadFailed)
                        }
                    }
                )
            } catch (e: Exception) {
                Log.e("SearchViewModel", "Search error", e)
                _uiState.update {
                    it.copy(searchResultUiState = SearchResultUiState.LoadFailed)
                }
            }
        }
    }

    fun loadMoreProducts() {
        val currentState = _uiState.value

        if (currentState.isLoadingMore || !currentState.hasMoreProducts) {
            return
        }

        if (currentState.searchResultUiState !is SearchResultUiState.Success) {
            return
        }

        _uiState.update { it.copy(isLoadingMore = true) }

        viewModelScope.launch {
            try {
                val currentProducts = currentState.searchResultUiState.products

                productsRepository.searchProducts(
                    query = currentState.searchQuery,
                    offset = currentState.currentOffset
                ).fold(
                    onSuccess = { searchResponse ->
                        Log.d(
                            "Pagination",
                            "Loaded ${searchResponse.results.size} more products, total: ${searchResponse.paging.total}"
                        )

                        val newProducts = currentProducts + searchResponse.results

                        val hasMore = searchResponse.results.isNotEmpty() &&
                                searchResponse.paging.total > (currentState.currentOffset + searchResponse.results.size)

                        _uiState.update {
                            it.copy(
                                searchResultUiState = SearchResultUiState.Success(newProducts),
                                isLoadingMore = false,
                                hasMoreProducts = hasMore,
                                currentOffset = currentState.currentOffset + searchResponse.results.size
                            )
                        }
                    },
                    onFailure = {
                        _uiState.update { it.copy(isLoadingMore = false) }
                    }
                )
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoadingMore = false) }
            }
        }
    }

}