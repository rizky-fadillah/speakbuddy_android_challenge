package jp.speakbuddy.edisonandroidexercise.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import jp.speakbuddy.edisonandroidexercise.data.local.model.CatFactEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CatFactDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCatFact(catFactEntity: CatFactEntity)

    @Query("SELECT * FROM catfactentity ORDER BY id DESC limit 1")
    fun getLatestCatFactEntity(): Flow<CatFactEntity?>
}