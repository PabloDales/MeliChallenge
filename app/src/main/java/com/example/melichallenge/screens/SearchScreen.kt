package com.example.melichallenge.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.melichallenge.nav.Screen
import com.example.melichallenge.ui.theme.MeliChallengeTheme
import com.example.melichallenge.ui.theme.PastelGreen

@Composable
fun SearchScreen(
    navController: NavHostController,
    searchViewModel: SearchViewModel,
    searchScreenViewModel: SearchScreenViewModel,
    requestFocusOnStart: Boolean = true
) {
    val searchUiState by searchViewModel.uiState.collectAsState()
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    var searchTextFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = searchUiState.searchQuery,
                selection = TextRange(searchUiState.searchQuery.length)
            )
        )
    }
    var showHistory by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (requestFocusOnStart) {
            try {
                focusRequester.requestFocus()
            } catch (_: Exception) {}
        }
        searchViewModel.loadSearchHistory()
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Surface(
                color = PastelGreen,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = searchTextFieldValue,
                    onValueChange = { newValue ->
                        searchTextFieldValue = newValue
                        showHistory = newValue.text.isEmpty()
                        searchViewModel.onSearchQueryChanged(newValue.text)
                        searchScreenViewModel.updateSearchQuery(newValue.text)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .focusRequester(focusRequester)
                        .onFocusChanged { focusState ->
                            if (focusState.isFocused) {
                                showHistory = searchTextFieldValue.text.isEmpty()
                                searchViewModel.onSearchFieldFocused()
                            }
                        },
                    placeholder = { Text("Buscar productos y más...") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                    },
                    trailingIcon = {
                        if (searchTextFieldValue.text.isNotEmpty()) {
                            IconButton(onClick = {
                                searchTextFieldValue = TextFieldValue(
                                    text = "",
                                    selection = TextRange(0)
                                )
                                showHistory = true
                                searchViewModel.onSearchQueryChanged("")
                                searchScreenViewModel.updateSearchQuery("")
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Limpiar"
                                )
                            }
                        }
                    },
                    shape = RoundedCornerShape(24.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        cursorColor = MaterialTheme.colorScheme.primary,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            if (searchTextFieldValue.text.isNotBlank()) {
                                keyboardController?.hide()

                                searchScreenViewModel.updateSearchQuery(searchTextFieldValue.text)

                                navController.navigate(Screen.Main.route) {
                                    popUpTo(Screen.Main.route) { inclusive = true }
                                }

                                searchViewModel.saveSearchToHistory(searchTextFieldValue.text)

                                searchScreenViewModel.onSearchTriggered(searchTextFieldValue.text)
                            }
                        }
                    )
                )
            }

            if (showHistory) {
                SearchHistorySection(
                    searchHistory = searchUiState.searchHistory,
                    onHistoryItemClick = { query ->
                        showHistory = false
                        keyboardController?.hide()

                        searchViewModel.onSearchQueryChanged(query)
                        searchScreenViewModel.updateSearchQuery(query)

                        navController.navigate(Screen.Main.route) {
                            popUpTo(Screen.Main.route) { inclusive = true }
                        }

                        searchViewModel.saveSearchToHistory(query)
                        searchScreenViewModel.onSearchTriggered(query)
                    }
                )
            }
        }
    }
}


@Composable
fun SearchHistorySection(
    searchHistory: List<String>,
    onHistoryItemClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp)
    ) {
        Text(
            text = "Búsquedas recientes",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        LazyColumn {
            items(searchHistory) { query ->
                SearchHistoryItem(
                    query = query,
                    onClick = { onHistoryItemClick(query) }
                )
            }
        }
    }
}

@Composable
fun SearchHistoryItem(
    query: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = query,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
    }

    HorizontalDivider(
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
    )
}


@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    MeliChallengeTheme {
        val sampleHistory = listOf(
            "iPhone 13 Pro Max",
            "Auriculares inalámbricos",
            "Zapatillas running",
            "Notebook Lenovo ThinkPad",
            "Cámara Sony Alpha"
        )

        Scaffold { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Surface(
                    color = PastelGreen,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = TextFieldValue(""),
                        onValueChange = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        placeholder = { Text("Buscar productos y más...") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Buscar"
                            )
                        },
                        trailingIcon = {},
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            cursorColor = MaterialTheme.colorScheme.primary,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        ),
                        singleLine = true
                    )
                }

                SearchHistorySection(
                    searchHistory = sampleHistory,
                    onHistoryItemClick = {}
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchHistoryItemPreview() {
    MeliChallengeTheme {
        Surface {
            SearchHistoryItem(
                query = "Auriculares inalámbricos",
                onClick = {}
            )
        }
    }
}
