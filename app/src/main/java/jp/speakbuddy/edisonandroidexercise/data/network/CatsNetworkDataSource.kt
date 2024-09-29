package jp.speakbuddy.edisonandroidexercise.data.network

interface CatsNetworkDataSource {

    suspend fun getCatFact(): NetworkCatFact
}