package jp.speakbuddy.edisonandroidexercise.data.testdoubles

import jp.speakbuddy.edisonandroidexercise.network.CatsNetworkDataSource
import jp.speakbuddy.edisonandroidexercise.network.model.NetworkCatFact

class TestCatsNetworkDataSource : CatsNetworkDataSource {

    override suspend fun getCatFact(): NetworkCatFact {
        return NetworkCatFact(
            fact = "This is a cat fact.",
            length = 19
        )
    }
}