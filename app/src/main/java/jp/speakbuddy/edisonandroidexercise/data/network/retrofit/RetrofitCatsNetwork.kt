package jp.speakbuddy.edisonandroidexercise.data.network.retrofit

import javax.inject.Inject
import javax.inject.Singleton
import jp.speakbuddy.edisonandroidexercise.data.network.CatsNetworkDataSource
import jp.speakbuddy.edisonandroidexercise.data.network.NetworkCatFact
import jp.speakbuddy.edisonandroidexercise.data.network.FactService

@Singleton
class RetrofitCatsNetwork @Inject constructor(private val factService: FactService) :
    CatsNetworkDataSource {

    override suspend fun getCatFact(): NetworkCatFact {
        return factService.getFact()
    }
}