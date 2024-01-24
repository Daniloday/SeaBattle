package com.missclick.seabattle.di

import com.missclick.seabattle.data.RepositoryImpl
import com.missclick.seabattle.data.remote.FireStore
import com.missclick.seabattle.domain.Repository
import com.missclick.seabattle.domain.use_cases.CreateRoomUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {



    @Provides
    @Singleton
    fun provideRepository(fireStore: FireStore) : Repository{
        return RepositoryImpl(fireStore)
    }



}