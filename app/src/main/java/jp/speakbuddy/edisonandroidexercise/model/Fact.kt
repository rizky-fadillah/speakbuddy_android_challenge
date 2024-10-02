package jp.speakbuddy.edisonandroidexercise.model

import jp.speakbuddy.edisonandroidexercise.domain.model.PresentableFact

data class Fact(val fact: String, val length: Int)

fun Fact.toPresentableFact(): PresentableFact {
    return PresentableFact(
        fact = fact,
        length = if (length > 100) length.toString() else null,
        shouldShowMultipleCats = fact.contains("cats")
    )
}