package com.example.melichallenge.screens

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melichallenge.data.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject



@HiltViewModel
class SearchViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    private val SEARCH_HISTORY_KEY = stringPreferencesKey("search_history")

    data class SearchUiState(
        val searchQuery: String = "",
        val isLoading: Boolean = false,
        val searchResults: Result<List<Product>> = Result.success(emptyList()),
        val searchHistory: List<String> = emptyList(),
        val showHistory: Boolean = false,
        val error: String? = null
    )

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    init {
        loadSearchHistory()
    }

    fun loadSearchHistory() {
        viewModelScope.launch {
            dataStore.data
                .catch { exception ->
                    if (exception is IOException) {
                        emit(emptyPreferences())
                    } else {
                        throw exception
                    }
                }
                .map { preferences ->
                    preferences[SEARCH_HISTORY_KEY]?.split(",") ?: emptyList()
                }
                .collect { historyList ->
                    _uiState.update { currentState ->
                        currentState.copy(searchHistory = historyList.filter { it.isNotBlank() })
                    }
                }
        }
    }

    fun saveSearchToHistory(query: String) {
        if (query.isBlank()) return

        viewModelScope.launch {
            val currentHistory = _uiState.value.searchHistory.toMutableList()

            currentHistory.remove(query)

            currentHistory.add(0, query)

            val limitedHistory = currentHistory.take(10)

            _uiState.update { it.copy(searchHistory = limitedHistory) }

            dataStore.edit { preferences ->
                preferences[SEARCH_HISTORY_KEY] = limitedHistory.joinToString(",")
            }
        }
    }

    fun onSearchFieldFocused() {
        _uiState.update { currentState ->
            currentState.copy(showHistory = currentState.searchQuery.isEmpty())
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { currentState ->
            currentState.copy(
                searchQuery = query,
                showHistory = query.isEmpty()
            )
        }
    }
}