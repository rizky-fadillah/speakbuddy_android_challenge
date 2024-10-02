package jp.speakbuddy.edisonandroidexercise.data.network.retrofit

import javax.inject.Inject
import javax.inject.Singleton
import jp.speakbuddy.edisonandroidexercise.data.network.CatsNetworkDataSource
import jp.speakbuddy.edisonandroidexercise.data.network.model.NetworkCatFact

@Singleton
class RetrofitCatsNetwork @Inject constructor(private val catFactService: CatFactService) :
    CatsNetworkDataSource {

    override suspend fun getCatFact(): NetworkCatFact {
        return catFactService.getFact()
    }
}