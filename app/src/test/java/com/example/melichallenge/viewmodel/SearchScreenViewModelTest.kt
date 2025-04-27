package com.example.melichallenge.viewmodel

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.melichallenge.data.model.Paging
import com.example.melichallenge.data.model.SearchResponse
import com.example.melichallenge.data.model.TestDataFactory
import com.example.melichallenge.repository.FakeProductRepository
import com.example.melichallenge.screens.SearchScreenViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SearchScreenViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var productRepository: FakeProductRepository
    private lateinit var viewModel: SearchScreenViewModel

    private val testDispatcher = StandardTestDispatcher()


    private val sampleProducts = TestDataFactory.sampleProducts

    private val searchResponse = SearchResponse(
        keywords = "smartphone",
        results = sampleProducts,
        paging = Paging(total = 100, offset = 0, limit = 20)
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0

        productRepository = mockk<FakeProductRepository>()
        viewModel = SearchScreenViewModel(productRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onSearchTriggered debería actualizar el estado con los resultados de búsqueda`() =
        runTest {
            val searchQuery = "smartphone"

            viewModel.updateSearchQuery(searchQuery)

            coEvery {
                productRepository.searchProducts(searchQuery, 0)
            } returns Result.success(searchResponse)

            viewModel.onSearchTriggered(searchQuery)

            testDispatcher.scheduler.advanceUntilIdle()

            val currentState = viewModel.uiState.first()

            assertEquals(searchQuery, currentState.searchQuery)
            assertTrue(currentState.searchResultUiState is SearchScreenViewModel.SearchResultUiState.Success)

        }
}