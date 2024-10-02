package jp.speakbuddy.edisonandroidexercise.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jp.speakbuddy.edisonandroidexercise.data.local.CatFactDatabase
import jp.speakbuddy.edisonandroidexercise.data.local.dao.CatFactDao

@Module
@InstallIn(SingletonComponent::class)
internal object DaosModule {

    @Provides
    fun providesCatFactDao(
        database: CatFactDatabase,
    ): CatFactDao = database.catFactDao()
}