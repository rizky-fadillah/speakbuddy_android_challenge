package jp.speakbuddy.edisonandroidexercise.data.repository

import javax.inject.Inject
import jp.speakbuddy.edisonandroidexercise.data.local.dao.CatFactDao
import jp.speakbuddy.edisonandroidexercise.data.local.model.asExternalModel
import jp.speakbuddy.edisonandroidexercise.data.model.asEntity
import jp.speakbuddy.edisonandroidexercise.data.network.CatsNetworkDataSource
import jp.speakbuddy.edisonandroidexercise.model.Fact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class DefaultCatsRepository @Inject constructor(
    private val network: CatsNetworkDataSource,
    private val catFactDao: CatFactDao
) : CatsRepository {

    override fun getLatestCatFact(): Flow<Fact?> =
        catFactDao.getLatestCatFactEntity().map { entity ->
            entity?.asExternalModel()
        }

    override suspend fun loadRandomCatFact() = withContext(Dispatchers.IO) {
        val networkResponse = network.getCatFact()

        catFactDao.insertCatFact(networkResponse.asEntity())
    }
}