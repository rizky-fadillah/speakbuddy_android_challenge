package jp.speakbuddy.edisonandroidexercise.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jp.speakbuddy.edisonandroidexercise.data.local.CatFactDao
import jp.speakbuddy.edisonandroidexercise.data.local.CatFactDatabase

@Module
@InstallIn(SingletonComponent::class)
internal object DaosModule {

    @Provides
    fun providesTopicsDao(
        database: CatFactDatabase,
    ): CatFactDao = database.catFactDao()
}