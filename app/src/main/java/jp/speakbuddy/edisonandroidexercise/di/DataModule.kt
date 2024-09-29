package jp.speakbuddy.edisonandroidexercise.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jp.speakbuddy.edisonandroidexercise.data.CatsRepository
import jp.speakbuddy.edisonandroidexercise.data.DefaultCatsRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    internal abstract fun bindsCatsRepository(
        catsRepository: DefaultCatsRepository,
    ): CatsRepository
}