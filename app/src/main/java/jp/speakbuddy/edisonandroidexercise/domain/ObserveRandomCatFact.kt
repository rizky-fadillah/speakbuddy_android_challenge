package jp.speakbuddy.edisonandroidexercise.domain

import javax.inject.Inject
import jp.speakbuddy.edisonandroidexercise.data.repository.CatsRepository
import jp.speakbuddy.edisonandroidexercise.model.Fact
import kotlinx.coroutines.flow.Flow

class ObserveRandomCatFact @Inject constructor(private val catsRepository: CatsRepository) {

    operator fun invoke(): Flow<Fact?> = catsRepository.getLatestCatFact()
}