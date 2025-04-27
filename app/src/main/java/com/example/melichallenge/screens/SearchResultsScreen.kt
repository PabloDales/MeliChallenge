package com.example.melichallenge.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.melichallenge.data.model.Attribute
import com.example.melichallenge.data.model.Picture
import com.example.melichallenge.data.model.Product
import com.example.melichallenge.data.model.Settings
import com.example.melichallenge.nav.Screen
import com.example.melichallenge.ui.theme.DarkGray
import com.example.melichallenge.ui.theme.LightGreen
import com.example.melichallenge.ui.theme.PastelGreen
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultScreen(
    navController: NavHostController,
    viewModel: SearchScreenViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            Surface(
                color = PastelGreen
            ) {
                TopAppBar(
                    title = {},
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = PastelGreen,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )

                SearchBar(
                    query = uiState.searchQuery,
                    isQueryActive = uiState.searchQuery.isNotEmpty(),
                    onSearchClick = {
                        navController.navigate(Screen.SearchHistory.route)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val searchResultState = uiState.searchResultUiState) {
                is SearchScreenViewModel.SearchResultUiState.Loading -> {
                    LoadingView()
                }

                is SearchScreenViewModel.SearchResultUiState.EmptyQuery -> {
                    EmptyQueryContent(
                        onSuggestionClick = { query ->
                            viewModel.updateSearchQuery(query)
                            viewModel.onSearchTriggered(query)
                        },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is SearchScreenViewModel.SearchResultUiState.LoadFailed -> {
                    ErrorMessage(
                        message = "No se pudieron cargar los resultados",
                        onRetry = {
                            if (uiState.searchQuery.isNotEmpty()) {
                                viewModel.onSearchTriggered(uiState.searchQuery)
                            }
                        },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is SearchScreenViewModel.SearchResultUiState.Success -> {
                    if (searchResultState.isEmpty()) {
                        NoResultsMessage(
                            query = uiState.searchQuery,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        val products = searchResultState.products
                        val listState = rememberLazyListState()

                        LaunchedEffect(products.size, uiState.isLoadingMore) {
                            Log.d(
                                "Pagination",
                                "Products count: ${products.size}, isLoadingMore: ${uiState.isLoadingMore}, hasMore: ${uiState.hasMoreProducts}"
                            )
                        }

                        LazyColumn(
                            state = listState
                        ) {
                            items(products) { product ->
                                ProductItemCard(
                                    product = product,
                                    onProductClick = {
                                        viewModel.selectProduct(product)
                                        product.id.let { id ->
                                            navController.navigate(Screen.Detail.createRoute(id))
                                        }
                                    }
                                )
                            }

                            item {
                                if (uiState.isLoadingMore) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(40.dp)
                                        )
                                    }
                                } else if (!uiState.hasMoreProducts && products.isNotEmpty()) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "No hay más productos",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                                alpha = 0.7f
                                            )
                                        )
                                    }
                                }
                            }
                        }


                        LaunchedEffect(listState, products.size) {
                            snapshotFlow {
                                val layoutInfo = listState.layoutInfo
                                val visibleItemsInfo = layoutInfo.visibleItemsInfo

                                if (visibleItemsInfo.isEmpty()) {
                                    false
                                } else {
                                    val lastVisibleItem = visibleItemsInfo.last()
                                    val lastIndex = lastVisibleItem.index
                                    val totalItemsCount = products.size

                                    lastIndex >= totalItemsCount - 3
                                }
                            }.distinctUntilChanged()
                                .collect { isNearEnd ->
                                    Log.d(
                                        "Pagination",
                                        "Near end: $isNearEnd, Loading: ${uiState.isLoadingMore}, HasMore: ${uiState.hasMoreProducts}, Products: ${products.size}"
                                    )

                                    if (isNearEnd && !uiState.isLoadingMore && uiState.hasMoreProducts) {
                                        Log.d("Pagination", "Loading more products")
                                        viewModel.loadMoreProducts()
                                    }
                                }
                        }
                    }
                }

                SearchScreenViewModel.SearchResultUiState.SearchNotReady -> {
                    SearchNotReadyView()
                }
            }
        }
    }
}


@Composable
fun SearchBar(
    query: String,
    isQueryActive: Boolean,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        onClick = onSearchClick,
        shape = RoundedCornerShape(24.dp),
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Buscar",
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )

            Text(
                text = query.ifEmpty { "Buscar en Mercado Libre" },
                style = MaterialTheme.typography.bodyLarge,
                color = if (isQueryActive) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EmptyQueryContent(
    onSuggestionClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Busca productos en MercadoLibre",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        FlowRow(
            modifier = Modifier.padding(8.dp)
        ) {
            listOf(
                "iPhone",
                "Notebook",
                "Auriculares",
                "Zapatillas",
                "SmartTV"
            ).forEach { suggestion ->
                SuggestionChip(
                    onClick = { onSuggestionClick(suggestion) },
                    label = { Text(suggestion) },
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
}

@Composable
fun ErrorMessage(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Ocurrió un error: $message",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Reintentar")
        }
    }
}

@Composable
fun NoResultsMessage(
    query: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            modifier = Modifier
                .size(72.dp)
                .padding(bottom = 16.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
        )

        Text(
            text = "No se encontraron resultados para \"$query\"",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Intenta con otras palabras o categorías",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun ProductItemCard(
    product: Product,
    onProductClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onProductClick)
            .padding(5.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 16.dp, top = 16.dp, bottom = 16.dp),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .background(
                            color = LightGreen,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = PastelGreen.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(10.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Box {
                        val imageUrl = product.pictures?.firstOrNull()?.url
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = product.name,
                            modifier = Modifier.size(80.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    val brand =
                        product.attributes?.find { it.id == "BRAND" }?.valueName ?: ""
                    Text(
                        text = brand.uppercase(),
                        style = MaterialTheme.typography.labelMedium,
                        color = DarkGray,
                        fontWeight = FontWeight.Normal
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = DarkGray,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    val model = product.attributes?.find { it.id == "MODEL" }?.valueName
                    model?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium,
                            color = DarkGray
                        )
                    }

                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        color = Color(0xFFEEEEEE)
                    )

                    val color = product.attributes?.find { it.id == "COLOR" }?.valueName
                    color?.let {
                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .background(color = Color(0xFF00A650), shape = CircleShape)
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = "Color: $it",
                                style = MaterialTheme.typography.bodySmall,
                                color = DarkGray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                }
            }

        }
    }
}

@Composable
fun LoadingView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(60.dp)
        )
    }
}

@Composable
fun SearchNotReadyView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}


@Preview(showBackground = true)
@Composable
fun SearchBarPreview() {
    MaterialTheme {
        Surface(
            color = PastelGreen,
            modifier = Modifier.padding(16.dp)
        ) {
            SearchBar(
                query = "Samsung Galaxy",
                isQueryActive = true,
                onSearchClick = {},
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EmptySearchBarPreview() {
    MaterialTheme {
        Surface(
            color = PastelGreen,
            modifier = Modifier.padding(16.dp)
        ) {
            SearchBar(
                query = "",
                isQueryActive = false,
                onSearchClick = {},
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductItemCardPreview() {
    val sampleProduct = Product(
        id = "MLA123456789",
        siteId = "MLA",
        name = "iPhone 13 Pro Max (256 GB) - Grafito",
        dateCreated = "2023-10-15T14:30:00Z",
        catalogProductId = "MLA123456789",
        status = "active",
        domainId = "MLA-CELLPHONES",
        settings = Settings(
            listingStrategy = "catalog_required",
            exclusive = false
        ),
        attributes = listOf(
            Attribute(
                id = "BRAND",
                name = "Marca",
                valueId = "9344",
                valueName = "Apple"
            ),
            Attribute(
                id = "MODEL",
                name = "Modelo",
                valueId = "1234",
                valueName = "iPhone 13 Pro Max"
            ),
            Attribute(
                id = "COLOR",
                name = "Color",
                valueId = "52049",
                valueName = "Grafito"
            ),
            Attribute(
                id = "IS_DUAL_SIM",
                name = "Es Dual SIM",
                valueId = "242085",
                valueName = "Sí"
            )
        ),
        pictures = listOf(
            Picture(
                id = "123456",
                url = "https://http2.mlstatic.com/D_NQ_NP_123456-MLA123456789_123456-F.jpg"
            )
        ),
        qualityType = "COMPLETE",
        priority = "MEDIUM",
        type = "PRODUCT",
        keywords = "iPhone 13 Pro Max Grafito 256GB"
    )

    MaterialTheme {
        ProductItemCard(
            product = sampleProduct,
            onProductClick = {}
        )
    }
}