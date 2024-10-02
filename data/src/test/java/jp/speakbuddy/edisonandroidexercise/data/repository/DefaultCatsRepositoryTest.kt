package jp.speakbuddy.edisonandroidexercise.data.repository

import jp.speakbuddy.edisonandroidexercise.data.testdoubles.TestCatFactDao
import jp.speakbuddy.edisonandroidexercise.data.testdoubles.TestCatsNetworkDataSource
import jp.speakbuddy.edisonandroidexercise.database.model.CatFactEntity
import jp.speakbuddy.edisonandroidexercise.database.model.asExternalModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DefaultCatsRepositoryTest {

    private val testScope = TestScope(UnconfinedTestDispatcher())

    private lateinit var subject: DefaultCatsRepository

    private lateinit var network: TestCatsNetworkDataSource

    private lateinit var catFactDao: TestCatFactDao

    @BeforeEach
    fun setUp() {
        network = TestCatsNetworkDataSource()
        catFactDao = TestCatFactDao()

        subject = DefaultCatsRepository(
            network = network, catFactDao = catFactDao
        )
    }

    @Test
    fun defaultCatsRepository_latest_cat_fact_stream_is_backed_by_cat_fact_dao() =
        testScope.runTest {
            val entityToInsert = CatFactEntity(
                fact = "This is a cat fact", length = 19
            )

            catFactDao.insertCatFact(entityToInsert)

            assertEquals(
                subject.getLatestCatFact().first(),
                catFactDao.getLatestCatFactEntity().first()?.asExternalModel()
            )
        }

    @Test
    fun defaultCatsRepository_cat_facts_stream_is_backed_by_cat_facts_dao() = testScope.runTest {
        val entity1 = CatFactEntity(
            fact = "This is a cat fact no. 1", length = 24
        )
        catFactDao.insertCatFact(entity1)

        val entity2 = CatFactEntity(
            fact = "This is a cat fact no. 2", length = 24
        )
        catFactDao.insertCatFact(entity2)

        val entity3 = CatFactEntity(
            fact = "This is a cat fact no. 3", length = 24
        )
        catFactDao.insertCatFact(entity3)

        assertEquals(subject.getCatFacts().first().size, 3)
        assertEquals(subject.getCatFacts().first(), catFactDao.getCatFactEntities().first().map {
            it.asExternalModel()
        })
    }

    @Test
    fun loadRandomCatFact() = testScope.runTest {
        subject.loadRandomCatFact()

        assertEquals(
            subject.getLatestCatFact().first(),
            catFactDao.getLatestCatFactEntity().first()?.asExternalModel()
        )
    }
}