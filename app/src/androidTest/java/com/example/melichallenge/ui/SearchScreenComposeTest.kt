package com.example.melichallenge.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import com.example.melichallenge.screens.SearchHistorySection
import com.example.melichallenge.screens.SearchScreen
import com.example.melichallenge.screens.SearchScreenViewModel
import com.example.melichallenge.screens.SearchViewModel
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock

class SearchScreenComposeTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var navController: TestNavHostController
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var searchScreenViewModel: SearchScreenViewModel
    private lateinit var uiStateFlow: MutableStateFlow<SearchViewModel.SearchUiState>

    @Before
    fun setup() {
        composeTestRule.mainClock.autoAdvance = false

        navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        searchViewModel = mockk<SearchViewModel>(relaxUnitFun = true)
        searchScreenViewModel = mockk<SearchScreenViewModel>(relaxUnitFun = true)
        uiStateFlow = MutableStateFlow(SearchViewModel.SearchUiState(
            searchHistory = listOf("smartphone", "notebook", "auriculares")
        ))

        every { searchViewModel.uiState } returns uiStateFlow
    }

    @Test
    fun searchField_isDisplayed() {
        composeTestRule.setContent {
            SearchScreen(
                navController = navController,
                searchViewModel = searchViewModel,
                searchScreenViewModel = searchScreenViewModel
            )
        }

        composeTestRule.onNodeWithText("Buscar productos y más...").assertIsDisplayed()
    }
    @Test
    fun searchHistoryItem_triggersSearch() {
        val onHistoryItemClickMock = mockk<(String) -> Unit>()

        every { onHistoryItemClickMock(any()) } just runs

        composeTestRule.setContent {
            SearchHistorySection(
                searchHistory = listOf("smartphone", "notebook", "auriculares"),
                onHistoryItemClick = onHistoryItemClickMock
            )
        }

        composeTestRule.onNodeWithText("notebook").performClick()

        verify { onHistoryItemClickMock("notebook") }
    }
}