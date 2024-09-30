package jp.speakbuddy.edisonandroidexercise.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import jp.speakbuddy.edisonandroidexercise.data.local.dao.CatFactDao
import jp.speakbuddy.edisonandroidexercise.data.local.model.CatFactEntity
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
    fun catFactDao_get_latest_cat_fact_entity() = runTest {
        val entity = CatFactEntity(
            id = 0,
            fact = "This is cat fact no. 1",
            length = 22
        )
        val entity2 = CatFactEntity(
            id = 1,
            fact = "This is cat fact no. 2",
            length = 22
        )

        catFactDao.insertCatFact(entity)
        catFactDao.insertCatFact(entity2)

        assertEquals(
            entity2,
            catFactDao.getLatestCatFactEntity().first()
        )
    }
}