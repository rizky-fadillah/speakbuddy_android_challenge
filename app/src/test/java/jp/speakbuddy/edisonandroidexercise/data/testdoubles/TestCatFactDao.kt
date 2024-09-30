package jp.speakbuddy.edisonandroidexercise.data.testdoubles

import jp.speakbuddy.edisonandroidexercise.data.local.dao.CatFactDao
import jp.speakbuddy.edisonandroidexercise.data.local.model.CatFactEntity
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

    override fun getLatestCatFactEntity(): Flow<CatFactEntity?> {
        return entitiesStateFlow.map {
            it.first()
        }
    }
}