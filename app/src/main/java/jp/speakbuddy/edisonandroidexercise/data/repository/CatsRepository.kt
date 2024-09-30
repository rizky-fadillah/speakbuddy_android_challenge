package jp.speakbuddy.edisonandroidexercise.data.repository

import jp.speakbuddy.edisonandroidexercise.model.Fact
import kotlinx.coroutines.flow.Flow

interface CatsRepository {

    fun getLatestCatFact(): Flow<Fact?>

    suspend fun loadRandomCatFact()
}
