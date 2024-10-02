package jp.speakbuddy.edisonandroidexercise.data.repository

import jp.speakbuddy.edisonandroidexercise.model.Fact
import kotlinx.coroutines.flow.Flow

interface CatsRepository {

    fun getLatestCatFact(): Flow<Fact?>

    fun getCatFacts(): Flow<List<Fact>>

    fun searchCatFacts(searchQuery: String): Flow<List<Fact>>

    suspend fun loadRandomCatFact()
}
