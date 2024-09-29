package jp.speakbuddy.edisonandroidexercise.data

import javax.inject.Inject
import jp.speakbuddy.edisonandroidexercise.data.local.CatFactDao
import jp.speakbuddy.edisonandroidexercise.data.local.asExternalModel
import jp.speakbuddy.edisonandroidexercise.data.model.asEntity
import jp.speakbuddy.edisonandroidexercise.data.network.CatsNetworkDataSource
import jp.speakbuddy.edisonandroidexercise.domain.model.Fact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class DefaultCatsRepository @Inject constructor(
    private val network: CatsNetworkDataSource,
    private val catFactDao: CatFactDao
) : CatsRepository {

    override fun getRecentCatFact(): Flow<Fact?> =
        catFactDao.getRecentCatFactEntity().map { entity ->
            entity?.asExternalModel()
        }

    override suspend fun refresh() = withContext(Dispatchers.IO) {
        val networkResponse = network.getCatFact()

        catFactDao.insertCatFact(networkResponse.asEntity())
    }
}