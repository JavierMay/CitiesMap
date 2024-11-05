package com.javimay.uala.di

import com.javimay.uala.data.repository.CitiesRepositoryImpl
import com.javimay.uala.domain.repository.ICitiesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface IRepositoryModule {

    @Binds
    fun provideCitiesRepository(
        citiesRepositoryImpl: CitiesRepositoryImpl
    ): ICitiesRepository
}