package jp.speakbuddy.edisonandroidexercise.data.network.retrofit

import jp.speakbuddy.edisonandroidexercise.data.network.model.NetworkCatFact
import retrofit2.http.GET

interface FactService {
    @GET("fact")
    suspend fun getFact(): NetworkCatFact
}
