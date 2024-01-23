package com.missclick.seabattle.di

import com.missclick.seabattle.data.RepositoryImpl
import com.missclick.seabattle.domain.Repository
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
    fun provideRepository() : Repository{
        return RepositoryImpl()
    }

}