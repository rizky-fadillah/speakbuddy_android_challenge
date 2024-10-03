package jp.speakbuddy.edisonandroidexercise.data.testdoubles

import jp.speakbuddy.edisonandroidexercise.database.dao.CatFactDao
import jp.speakbuddy.edisonandroidexercise.database.model.CatFactEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class TestCatFactDao : CatFactDao {

    private val entitiesStateFlow = MutableStateFlow(emptyList<CatFactEntity>())

    override suspend fun insertCatFact(catFactEntity: CatFactEntity) {
        entitiesStateFlow.update { oldValues ->
            oldValues + catFactEntity
        }
    }

    override fun getLatestCatFactEntity(): Flow<CatFactEntity?> {
        return entitiesStateFlow.map {
            it.first()
        }
    }

    override fun getCatFactEntities(): Flow<List<CatFactEntity>> = entitiesStateFlow

    override fun getCatFactEntitiesByQuery(searchQuery: String) = entitiesStateFlow.map {
        it.filter { fact -> fact.fact.contains(searchQuery) }
    }
}