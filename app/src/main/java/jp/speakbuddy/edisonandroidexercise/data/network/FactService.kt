package jp.speakbuddy.edisonandroidexercise.data.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET

interface FactService {
    @GET("fact")
    suspend fun getFact(): NetworkCatFact
}

@Serializable
data class NetworkCatFact(
    val fact: String,
    val length: Int
)

object FactServiceProvider {
    fun provide(): FactService =
        Retrofit.Builder()
            .baseUrl("https://catfact.ninja/")
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(FactService::class.java)
}
