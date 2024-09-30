package jp.speakbuddy.edisonandroidexercise.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkCatFact(
    val fact: String,
    val length: Int
)