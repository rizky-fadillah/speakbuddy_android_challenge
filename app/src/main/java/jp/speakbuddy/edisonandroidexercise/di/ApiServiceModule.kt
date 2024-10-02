package jp.speakbuddy.edisonandroidexercise.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import jp.speakbuddy.edisonandroidexercise.network.retrofit.CatFactService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class ApiServiceModule {

    @Singleton
    @Provides
    fun provideCatFactService(): CatFactService {
        return Retrofit.Builder()
            .baseUrl("https://catfact.ninja/")
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(CatFactService::class.java)
    }
}