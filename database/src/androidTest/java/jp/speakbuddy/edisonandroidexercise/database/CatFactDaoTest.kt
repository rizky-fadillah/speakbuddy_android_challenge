package jp.speakbuddy.edisonandroidexercise.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import jp.speakbuddy.edisonandroidexercise.database.dao.CatFactDao
import jp.speakbuddy.edisonandroidexercise.database.model.CatFactEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CatFactDaoTest {

    private lateinit var catFactDao: CatFactDao

    private lateinit var db: CatFactDatabase

    @BeforeEach
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            CatFactDatabase::class.java,
        ).build()
        catFactDao = db.catFactDao()
    }

    @AfterEach
    fun closeDb() = db.close()

    @Test
    fun testGetLatestCatFact_ReturnsMostRecentEntity() = runTest {
        // Arrange: Prepare the cat fact entities to insert into the database
        val entity1 = CatFactEntity(
            id = 0,
            fact = "This is cat fact no. 1",
            length = 22
        )
        val entity2 = CatFactEntity(
            id = 1,
            fact = "This is cat fact no. 2",
            length = 22
        )

        // Act: Insert the entities and retrieve the latest cat fact
        catFactDao.insertCatFact(entity1)
        catFactDao.insertCatFact(entity2)
        val latestFact = catFactDao.getLatestCatFactEntity().first()

        // Assert: Verify that the most recent entity is returned
        assertEquals(entity2, latestFact)
    }
}