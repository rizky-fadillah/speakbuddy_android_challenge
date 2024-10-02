package jp.speakbuddy.edisonandroidexercise.domain.model

import jp.speakbuddy.edisonandroidexercise.model.Fact

data class PresentableFact(
    val fact: String,
    val length: String?,
    val shouldShowMultipleCats: Boolean
)

fun Fact.toPresentableFact(): PresentableFact {
    return PresentableFact(
        fact = fact,
        length = if (length > 100) length.toString() else null,
        shouldShowMultipleCats = fact.contains("cats", ignoreCase = true)
    )
}