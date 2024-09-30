package jp.speakbuddy.edisonandroidexercise.data.repository

import jp.speakbuddy.edisonandroidexercise.data.local.dao.CatFactDao
import jp.speakbuddy.edisonandroidexercise.data.local.model.CatFactEntity
import jp.speakbuddy.edisonandroidexercise.data.local.model.asExternalModel
import jp.speakbuddy.edisonandroidexercise.data.testdoubles.TestCatFactDao
import jp.speakbuddy.edisonandroidexercise.data.testdoubles.TestCatsNetworkDataSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DefaultCatsRepositoryTest {

    private val testScope = TestScope(UnconfinedTestDispatcher())

    private lateinit var subject: DefaultCatsRepository

    private lateinit var network: TestCatsNetworkDataSource

    private lateinit var catFactDao: CatFactDao

    @BeforeEach
    fun setUp() {
        network = TestCatsNetworkDataSource()
        catFactDao = TestCatFactDao()

        subject = DefaultCatsRepository(
            network = network,
            catFactDao = catFactDao
        )
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun defaultCatsRepository_cat_fact_stream_is_backed_by_cat_fact_dao() = testScope.runTest {
        val entityToInsert = CatFactEntity(
            fact = "This is a cat fact",
            length = 19
        )

        catFactDao.insertCatFact(entityToInsert)

        assertEquals(
            subject.getLatestCatFact().first(),
            entityToInsert.asExternalModel()
        )
        assertEquals(
            subject.getLatestCatFact().first(),
            catFactDao.getLatestCatFactEntity().first()?.asExternalModel()
        )
    }

    @Test
    fun refresh() = testScope.runTest {
        subject.loadRandomCatFact()

        assertEquals(
            catFactDao.getLatestCatFactEntity().first(),
            CatFactEntity(
                id = 0,
                fact = "This is a cat fact.",
                length = 19
            )
        )
    }
}