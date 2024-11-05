package com.javimay.uala.di

import com.javimay.uala.data.api.IUalaService
import com.javimay.uala.data.repository.CitiesRemoteDataSourceImpl
import com.javimay.uala.data.repository.ICitiesRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteDataModule {

    @Singleton
    @Provides
    fun provideCitiesRemoteDataSource(ualaService: IUalaService): ICitiesRemoteDataSource =
        CitiesRemoteDataSourceImpl(ualaService)
}