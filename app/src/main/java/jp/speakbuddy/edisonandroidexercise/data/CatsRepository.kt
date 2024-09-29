package jp.speakbuddy.edisonandroidexercise.data

import jp.speakbuddy.edisonandroidexercise.domain.model.Fact
import kotlinx.coroutines.flow.Flow

interface CatsRepository {

    fun getRecentCatFact(): Flow<Fact?>

    suspend fun refresh()
}
