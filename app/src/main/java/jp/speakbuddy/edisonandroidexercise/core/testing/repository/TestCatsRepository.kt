package jp.speakbuddy.edisonandroidexercise.core.testing.repository

import jp.speakbuddy.edisonandroidexercise.data.repository.CatsRepository
import jp.speakbuddy.edisonandroidexercise.model.Fact
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class TestCatsRepository : CatsRepository {

    /**
     * The backing hot flow for the latest cat fact for testing.
     */
    private val catFactFlow: MutableSharedFlow<Fact> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override fun getLatestCatFact(): Flow<Fact?> = catFactFlow

    override suspend fun loadRandomCatFact() {
    }

    fun sendRandomCatFact(fact: Fact) {
        catFactFlow.tryEmit(fact)
    }
}