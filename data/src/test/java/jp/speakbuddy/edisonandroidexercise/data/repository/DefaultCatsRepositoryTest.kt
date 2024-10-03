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
            network = network,
            catFactDao = catFactDao
        )
    }

    @Test
    fun latestCatFactStream_isBackedByCatFactDao() = testScope.runTest {
        // Arrange
        val entityToInsert = CatFactEntity(fact = "This is a cat fact", length = 19)
        catFactDao.insertCatFact(entityToInsert)

        // Act
        val latestCatFact = subject.getLatestCatFact().first()
        val expectedCatFact = catFactDao.getLatestCatFactEntity().first()?.asExternalModel()

        // Assert
        assertEquals(expectedCatFact, latestCatFact)
    }

    @Test
    fun catFactsStream_isBackedByCatFactsDao() = testScope.runTest {
        // Arrange
        val entities = listOf(
            CatFactEntity(fact = "This is a cat fact no. 1", length = 24),
            CatFactEntity(fact = "This is a cat fact no. 2", length = 24),
            CatFactEntity(fact = "This is a cat fact no. 3", length = 24)
        )
        entities.forEach { catFactDao.insertCatFact(it) }

        // Act
        val catFactsFromRepository = subject.getCatFacts().first()
        val catFactsFromDao = catFactDao.getCatFactEntities().first().map { it.asExternalModel() }

        // Assert
        assertEquals(3, catFactsFromRepository.size)
        assertEquals(catFactsFromDao, catFactsFromRepository)
    }

    @Test
    fun loadRandomCatFact_insertsFactIntoDao() = testScope.runTest {
        // Act
        subject.loadRandomCatFact()

        // Assert
        val latestCatFact = subject.getLatestCatFact().first()
        val expectedCatFact = catFactDao.getLatestCatFactEntity().first()?.asExternalModel()

        assertEquals(expectedCatFact, latestCatFact)
    }
}