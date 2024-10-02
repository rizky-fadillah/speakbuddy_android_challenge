package jp.speakbuddy.edisonandroidexercise.network.retrofit

import javax.inject.Inject
import javax.inject.Singleton
import jp.speakbuddy.edisonandroidexercise.network.CatsNetworkDataSource
import jp.speakbuddy.edisonandroidexercise.network.model.NetworkCatFact

@Singleton
class RetrofitCatsNetwork @Inject constructor(private val catFactService: CatFactService) :
    CatsNetworkDataSource {

    override suspend fun getCatFact(): NetworkCatFact {
        return catFactService.getFact()
    }
}