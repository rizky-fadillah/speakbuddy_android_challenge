package jp.speakbuddy.edisonandroidexercise.core.testing.repository

import jp.speakbuddy.edisonandroidexercise.data.repository.CatsRepository
import jp.speakbuddy.edisonandroidexercise.model.Fact
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map

class TestCatsRepository : CatsRepository {

    /**
     * The backing hot flow for the latest cat fact for testing.
     */
    private val factsFlow: MutableSharedFlow<List<Fact>> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override fun getLatestCatFact(): Flow<Fact?> = factsFlow.map {
        it.firstOrNull()
    }

    override fun getCatFacts(): Flow<List<Fact>> = factsFlow

    override fun searchCatFacts(searchQuery: String): Flow<List<Fact>> {
        return factsFlow
    }

    override suspend fun loadRandomCatFact() {
    }

    /**
     * A test-only API to allow controlling the list of facts from tests.
     */
    fun sendRandomCatFact(fact: Fact) {
        factsFlow.tryEmit(listOf(fact))
    }

    /**
     * A test-only API to allow emitting empty list to emulate the empty data scenario in local db.
     */
    fun setFlowEmpty() {
        factsFlow.tryEmit(emptyList())
    }
}