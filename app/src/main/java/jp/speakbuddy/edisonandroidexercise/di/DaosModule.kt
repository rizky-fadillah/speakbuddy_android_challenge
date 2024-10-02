package jp.speakbuddy.edisonandroidexercise.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jp.speakbuddy.edisonandroidexercise.database.CatFactDatabase
import jp.speakbuddy.edisonandroidexercise.database.dao.CatFactDao

@Module
@InstallIn(SingletonComponent::class)
internal object DaosModule {

    @Provides
    fun providesCatFactDao(
        database: CatFactDatabase,
    ): CatFactDao = database.catFactDao()
}