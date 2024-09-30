package jp.speakbuddy.edisonandroidexercise.data.network

import jp.speakbuddy.edisonandroidexercise.data.network.model.NetworkCatFact

interface CatsNetworkDataSource {

    suspend fun getCatFact(): NetworkCatFact
}