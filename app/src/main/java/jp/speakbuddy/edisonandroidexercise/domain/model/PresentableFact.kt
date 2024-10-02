package jp.speakbuddy.edisonandroidexercise.domain.model

data class PresentableFact(
    val fact: String,
    val length: String?,
    val shouldShowMultipleCats: Boolean
)