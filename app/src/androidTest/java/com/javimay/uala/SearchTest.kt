package com.javimay.uala

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isFocusable
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.javimay.uala.data.model.CitiesState
import com.javimay.uala.ui.screens.CitiesViewModel
import com.javimay.uala.ui.screens.PortraitScreen
import com.javimay.uala.utils.getCitiesListMocked
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.internal.wait
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock

@HiltAndroidTest
class SearchTest {

    @get:Rule
    val rule = createComposeRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private lateinit var viewModelMocked: CitiesViewModel

    @Before
    fun init() {
        hiltRule.inject()
        viewModelMocked = mockk<CitiesViewModel>()
        val stateFlowState = mock(MutableStateFlow::class.java) as MutableStateFlow<CitiesState>
        val successStateMocked = mockk<CitiesState.Success>()
//        every { viewModelMocked.state } returns stateFlowState
//        every { viewModelMocked.state.value } returns successStateMocked
        /*coEvery { viewModelMocked.state.collect(any()) } answers {
            val collector = it.invocation.args[0] as (CitiesState) -> Unit
            collector(successStateMocked) as Nothing
        }*/
    }

    /*Given touch in searchbar
    When i want to search a city
    Then hint disappear*/
    @Test
    fun enterText_hintDisappear() {
        val emptyText = ""
        val textField = hasText("") and isFocusable()
        val hintText = hasText("Search...")

        every { viewModelMocked.updateTextSearch(emptyText) } returns Unit
        every { viewModelMocked.searchFavoritesEnabled } returns false
        every { viewModelMocked.textSearch } returns emptyText
        every { viewModelMocked.searchCities(emptyText) } returns Unit
        every { viewModelMocked.onSearchFavoritesEnabled(false) } returns Unit

        rule.setContent {
            PortraitScreen(citiesList = getCitiesListMocked(), viewModel = viewModelMocked)
        }
        rule.onNode(hintText).assertExists()
        rule.onNode(textField).performClick()
        rule.onNode(hintText).assertDoesNotExist()
    }

    /*Given enter a text in searchbar and click in clear icon
    When i want to search a city
    Then text is cleared and hint appear*/
    @Test
    fun enterText_clickInClearIcon_hintAppear() {
        val testText = "Al"
        val hintText = hasText("Search...")

        every { viewModelMocked.searchFavoritesEnabled } returns false
        every { viewModelMocked.textSearch } returns testText
        every { viewModelMocked.updateTextSearch(any()) } returns Unit
        every { viewModelMocked.searchCities(any()) } returns Unit
        every { viewModelMocked.onSearchFavoritesEnabled(false) } returns Unit

        rule.setContent {
            PortraitScreen(citiesList = getCitiesListMocked(), viewModel = viewModelMocked)
        }
        rule.onNode(hintText).assertDoesNotExist()
        rule.onNodeWithContentDescription("Clear Search").performClick()
        rule.onNode(hintText).assertExists()
    }

    /*Given enter Sy in searchbar
    When i want to search a city
    Then Sydney city should appear, */
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun enterSyText_SydneyCityAppear() {
        val sydneyText = "S"
        val sydneyItem = hasText("Sydney")
        val successStateMocked = mockk<CitiesState.Success>()
        val citiesListMocked = getCitiesListMocked()

        every { viewModelMocked.searchFavoritesEnabled } returns false
        every { viewModelMocked.updateTextSearch("") } returns Unit
        every { viewModelMocked.updateTextSearch(sydneyText) } returns Unit
        every { viewModelMocked.textSearch } returns ""
        every { viewModelMocked.searchCities("", false) } returns Unit
        every { viewModelMocked.searchCities(sydneyText) } returns Unit
        every { viewModelMocked.onSearchFavoritesEnabled(false) } returns Unit
        /*every { successStateMocked.cities } returns citiesListMocked.filter {
            it.name.contains(sydneyText, ignoreCase = true)
        }*/
       /* coEvery { viewModelMocked.state.collect(any()) } answers {
            val collector = it.invocation.args[0] as (CitiesState) -> Unit
            collector(successStateMocked) as Nothing
        }*/

        rule.setContent {
            PortraitScreen(
                citiesList = citiesListMocked,
                viewModel = viewModelMocked)
        }
        rule.onNodeWithContentDescription("Search text field").performTextInput(sydneyText)
        every { successStateMocked.cities } returns citiesListMocked.filter {
            it.name.contains(sydneyText, ignoreCase = true)
        }
         coEvery { viewModelMocked.state.collect(any()) } answers {
             val collector = it.invocation.args[0] as (CitiesState) -> Unit
             collector(successStateMocked) as Nothing
         }

        rule.waitUntilDoesNotExist(hasText("US"), 500000) /*{
            rule.onNodeWithText("US").isNotDisplayed()
        }*/
        rule.onNode(sydneyItem).assertExists()
    }
}