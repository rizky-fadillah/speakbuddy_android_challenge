package jp.speakbuddy.edisonandroidexercise.data.model

import jp.speakbuddy.edisonandroidexercise.data.local.model.CatFactEntity
import jp.speakbuddy.edisonandroidexercise.data.network.model.NetworkCatFact

fun NetworkCatFact.asEntity() = CatFactEntity(
    fact = fact,
    length = length
)