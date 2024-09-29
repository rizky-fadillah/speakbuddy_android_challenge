package jp.speakbuddy.edisonandroidexercise.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import jp.speakbuddy.edisonandroidexercise.data.network.FactService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class ApiServiceModule {

//    @Singleton
//    @Provides
//    fun provideOkHttpClient(): OkHttpClient {
//        return OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
//            level = HttpLoggingInterceptor.Level.BODY
//        }).build()
//    }

//    @Singleton
//    @Provides
//    fun provideRetrofit(okHttpClient: OkHttpClient): FactService {
//        return Retrofit.Builder().baseUrl(BASE_URL)
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//            .addConverterFactory(MoshiConverterFactory.create()).client(okHttpClient).build()
//            .create(FactService::class.java)
//    }

    @Singleton
    @Provides
    fun provideFactService(): FactService {
        return Retrofit.Builder()
            .baseUrl("https://catfact.ninja/")
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(FactService::class.java)
    }
}