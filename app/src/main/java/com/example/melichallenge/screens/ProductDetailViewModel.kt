package com.example.melichallenge.screens

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melichallenge.data.model.Product
import com.example.melichallenge.data.model.ProductDetail
import com.example.melichallenge.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    sealed class DetailUiState {
        data object Loading : DetailUiState()
        data class Success(val product: ProductDetail) : DetailUiState()
        data class Error(val message: String) : DetailUiState()
    }

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    private val productId: String = checkNotNull(savedStateHandle["productId"])

    init {
        loadProductDetails()
    }

    fun loadProductDetails() {
        viewModelScope.launch {
            _uiState.value = DetailUiState.Loading

            try {
                val product = productRepository.getProductById(productId)
                _uiState.value = DetailUiState.Success(product)
            } catch (e: Exception) {
                _uiState.value = DetailUiState.Error(e.message ?: "Error desconocido al cargar el producto")
            }
        }
    }
}