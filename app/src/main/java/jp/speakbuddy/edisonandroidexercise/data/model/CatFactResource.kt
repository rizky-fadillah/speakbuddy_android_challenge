package jp.speakbuddy.edisonandroidexercise.data.model

import jp.speakbuddy.edisonandroidexercise.data.local.CatFactEntity
import jp.speakbuddy.edisonandroidexercise.data.network.NetworkCatFact

fun NetworkCatFact.asEntity() = CatFactEntity(
    fact = fact,
    length = length
)