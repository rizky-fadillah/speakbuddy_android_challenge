package jp.speakbuddy.edisonandroidexercise.data.model

import jp.speakbuddy.edisonandroidexercise.database.model.CatFactEntity
import jp.speakbuddy.edisonandroidexercise.network.model.NetworkCatFact

fun NetworkCatFact.asEntity() = CatFactEntity(
    fact = fact,
    length = length
)