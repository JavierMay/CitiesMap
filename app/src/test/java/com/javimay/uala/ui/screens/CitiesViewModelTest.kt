package com.javimay.uala.ui.screens

import android.content.Context
import android.os.Looper
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.runtime.MutableState
import com.javimay.uala.data.model.CitiesState
import com.javimay.uala.data.model.City
import com.javimay.uala.domain.usecases.GetCitiesUseCase
import com.javimay.uala.utils.getCitiesListMocked
import com.javimay.uala.utils.getRandomCity
import io.mockk.awaits
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.withContext
import okhttp3.internal.wait
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.util.concurrent.CountDownLatch

@ExperimentalCoroutinesApi
class CitiesViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockLooper: Looper

    @Mock
    private lateinit var context: Context

    private val dispatcher = StandardTestDispatcher()
    private val useCase: GetCitiesUseCase = mockk()
    private lateinit var viewModel: CitiesViewModel

    @Before
    fun setup() {
        clearAllMocks()
        MockitoAnnotations.openMocks(this)
        viewModel = CitiesViewModel(useCase)
        Dispatchers.setMain(dispatcher)
        context = mock(Context::class.java)
        `when`(context.mainLooper).thenReturn(mockLooper)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Given getCities call When cities screen is loading Then return loading state`() {
        coEvery { useCase.execute() } returns getCitiesListMocked()
        viewModel.getCities()
        val result: CitiesState = viewModel.state.value
        assertTrue(result is CitiesState.Loading)
    }

    @Test
    fun `Given getCities call When cities screen is loading Then return Success state after scope launch`() {
        val mockedCities = getCitiesListMocked()
        coEvery { useCase.execute() } returns mockedCities
        val states = mutableListOf<CitiesState>()
        MainScope().launch {
            viewModel.getCities()
            states.add(viewModel.state.value)
        }
        dispatcher.scheduler.advanceUntilIdle()
        states.add(viewModel.state.value)
        assertTrue(states.contains(CitiesState.Success(mockedCities)))
    }

    @Test
    fun `Given getCities call When cities screen is loading Then return data in success state`() {
        val mockedCities = getCitiesListMocked()
        coEvery { useCase.execute() } returns mockedCities
        val states = mutableListOf<CitiesState>()
        MainScope().launch {
            viewModel.getCities()
            states.add(viewModel.state.value)
        }
        dispatcher.scheduler.advanceUntilIdle()
        states.add(viewModel.state.value)
        val successState = states.find { it is CitiesState.Success } as CitiesState.Success
        assertTrue(successState.cities.isNotEmpty())
    }

    @Test
    fun `Given a random city When city is favorite Then return city is favorite value as true`() {
        val citiesListField = CitiesViewModel::class.java.getDeclaredField("citiesList")
        citiesListField.isAccessible = true
        val citiesList = citiesListField.get(viewModel) as MutableState<List<City>>
        citiesList.value = getCitiesListMocked()
        val mockedCity = getRandomCity()
        var cityEdited = citiesList.value.find { it.id == mockedCity.id }
        MainScope().launch {
            assertFalse(cityEdited?.isFavorite ?: true)
            viewModel.toggleFavorite(mockedCity)
        }
        dispatcher.scheduler.advanceUntilIdle()
        val state = viewModel.state.value as CitiesState.Success
        cityEdited = state.cities.find { it.id == mockedCity.id }
        assertTrue(cityEdited?.isFavorite ?: false)
    }

    @Test
    fun `Given Al as search text When is searching a city Then return 3 cities with Al in name`() {
        val citiesListField = CitiesViewModel::class.java.getDeclaredField("citiesList")
        citiesListField.isAccessible = true
        val citiesList = citiesListField.get(viewModel) as MutableState<List<City>>
        citiesList.value = getCitiesListMocked()
        val queryToSearch = "Al"

        runTest {
            viewModel.searchCities(queryToSearch)
            advanceUntilIdle()
        }
        val state = viewModel.state.value
        println("State: $state")
        assertTrue((state as CitiesState.Success).cities.size == 3)
    }

    @Test
    fun `Given West as search text When is searching a city Then return West Palm Beach city`() {
        val citiesListField = CitiesViewModel::class.java.getDeclaredField("citiesList")
        citiesListField.isAccessible = true
        val citiesList = citiesListField.get(viewModel) as MutableState<List<City>>
        citiesList.value = getCitiesListMocked()
        val queryToSearch = "West"

        runTest {
            viewModel.searchCities(queryToSearch)
            advanceUntilIdle()
        }
        val state = viewModel.state.value
        println("State: $state")
        assertTrue((state as CitiesState.Success).cities.size == 1)
        assertTrue(state.cities.first().name == "West Palm Beach")
    }

    @Test
    fun `Given empty search text When is searching a city Then return the full list`() {
        val citiesListField = CitiesViewModel::class.java.getDeclaredField("citiesList")
        citiesListField.isAccessible = true
        val citiesList = citiesListField.get(viewModel) as MutableState<List<City>>
        citiesList.value = getCitiesListMocked()
        val queryToSearch = ""

        runTest {
            viewModel.searchCities(queryToSearch)
            advanceUntilIdle()
        }
        val state = viewModel.state.value
        println("State: $state")
        assertTrue(state is CitiesState.Success)
        val stateCitiesList = (state as CitiesState.Success).cities
        assertTrue(stateCitiesList.size == 5)
    }

    @Test
    fun `Given special character as search text When is searching a city Then return an empty list`() {
        val citiesListField = CitiesViewModel::class.java.getDeclaredField("citiesList")
        citiesListField.isAccessible = true
        val citiesList = citiesListField.get(viewModel) as MutableState<List<City>>
        citiesList.value = getCitiesListMocked()
        val queryToSearch = "@"

        runTest {
            viewModel.searchCities(queryToSearch)
            advanceUntilIdle()
        }
        dispatcher.scheduler.advanceUntilIdle()
        val state = viewModel.state.value
        println("State: $state")
        assertTrue(state is CitiesState.Success)
        val stateCitiesList = (state as CitiesState.Success).cities
        assertTrue(stateCitiesList.isEmpty())
    }

    @Test
    fun `Given Al as search text When is searching a city with favorites enable Then return 1 favorite city`() {
        val citiesListField = CitiesViewModel::class.java.getDeclaredField("citiesList")
        citiesListField.isAccessible = true
        val citiesList = citiesListField.get(viewModel) as MutableState<List<City>>
        citiesList.value = getCitiesListMocked()
        val queryToSearch = "Al"

        runTest {
            viewModel.searchCities(queryToSearch, true)
            advanceUntilIdle()
        }
        val state = viewModel.state.value
        println("State: $state")
        assertTrue(state is CitiesState.Success)
        val stateCitiesList = (state as CitiesState.Success).cities
        assertTrue(stateCitiesList.size == 1)
    }
}