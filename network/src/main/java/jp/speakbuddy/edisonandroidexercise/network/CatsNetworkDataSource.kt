package jp.speakbuddy.edisonandroidexercise.network

import jp.speakbuddy.edisonandroidexercise.network.model.NetworkCatFact

interface CatsNetworkDataSource {

    suspend fun getCatFact(): NetworkCatFact
}