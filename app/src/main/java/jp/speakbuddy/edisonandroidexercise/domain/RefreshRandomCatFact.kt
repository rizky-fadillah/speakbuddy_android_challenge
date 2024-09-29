package jp.speakbuddy.edisonandroidexercise.domain

import javax.inject.Inject
import jp.speakbuddy.edisonandroidexercise.data.CatsRepository

class RefreshRandomCatFact @Inject constructor(private val catsRepository: CatsRepository) {

    suspend operator fun invoke() {
        return catsRepository.refresh()
    }
}