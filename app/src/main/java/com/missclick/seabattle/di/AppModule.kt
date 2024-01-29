package com.missclick.seabattle.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.missclick.seabattle.data.RepositoryImpl
import com.missclick.seabattle.data.remote.FireStore
import com.missclick.seabattle.domain.Repository
import com.missclick.seabattle.domain.model.Settings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {



    @Provides
    @Singleton
    fun provideRepository(fireStore: FireStore) : Repository{
        return RepositoryImpl(fireStore)
    }

//    @Provides
//    @Singleton
//    internal fun provideSettingsDataStore(
//        @ApplicationContext context: Context,
//        ioDispatcher: CoroutineDispatcher,
//        scope: CoroutineScope,
//        userPreferencesSerializer: UserPreferencesSerializer,
//    ): DataStore<Settings> =
//        DataStoreFactory.create(
//            serializer = userPreferencesSerializer,
//            scope = CoroutineScope(scope.coroutineContext + ioDispatcher),
//            migrations = listOf(
//                IntToStringIdsMigration,
//            ),
//        ) {
//            context.dataStoreFile("user_preferences.pb")
//        }



}