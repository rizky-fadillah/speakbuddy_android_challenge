package jp.speakbuddy.edisonandroidexercise.data.testdoubles

import jp.speakbuddy.edisonandroidexercise.data.local.CatFactDao
import jp.speakbuddy.edisonandroidexercise.data.local.CatFactEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class TestCatFactDao : CatFactDao {

    private val entitiesStateFlow = MutableStateFlow(emptyList<CatFactEntity?>())

    override suspend fun insertCatFact(catFactEntity: CatFactEntity) {
        entitiesStateFlow.update { oldValues ->
            (oldValues + catFactEntity).distinctBy {
                it?.id
            }
        }
    }

    override fun getRecentCatFactEntity(): Flow<CatFactEntity?> {
        return entitiesStateFlow.map {
            it.first()
        }
    }
}