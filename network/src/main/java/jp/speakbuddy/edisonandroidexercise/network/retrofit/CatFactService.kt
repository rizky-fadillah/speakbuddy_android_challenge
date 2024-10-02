package jp.speakbuddy.edisonandroidexercise.network.retrofit

import jp.speakbuddy.edisonandroidexercise.network.model.NetworkCatFact
import retrofit2.http.GET

interface CatFactService {

    @GET("fact")
    suspend fun getFact(): NetworkCatFact
}
