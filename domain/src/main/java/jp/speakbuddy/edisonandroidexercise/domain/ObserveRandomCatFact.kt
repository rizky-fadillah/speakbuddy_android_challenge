package jp.speakbuddy.edisonandroidexercise.domain

import javax.inject.Inject
import jp.speakbuddy.edisonandroidexercise.data.repository.CatsRepository
import jp.speakbuddy.edisonandroidexercise.model.PresentableFact
import jp.speakbuddy.edisonandroidexercise.model.toPresentableFact
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ObserveRandomCatFact @Inject constructor(private val catsRepository: CatsRepository) {

    operator fun invoke(): Flow<PresentableFact?> = catsRepository.getLatestCatFact().map {
        it?.toPresentableFact()
    }
}