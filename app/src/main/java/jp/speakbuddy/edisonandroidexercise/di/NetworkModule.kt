package jp.speakbuddy.edisonandroidexercise.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jp.speakbuddy.edisonandroidexercise.network.CatsNetworkDataSource
import jp.speakbuddy.edisonandroidexercise.network.retrofit.RetrofitCatsNetwork

@Module
@InstallIn(SingletonComponent::class)
internal interface NetworkModule {

    @Binds
    fun binds(impl: RetrofitCatsNetwork): CatsNetworkDataSource
}
