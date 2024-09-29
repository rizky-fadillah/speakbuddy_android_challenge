package jp.speakbuddy.edisonandroidexercise.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CatFactDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCatFact(catFactEntity: CatFactEntity)

    @Query("SELECT * FROM catfactentity ORDER BY id DESC limit 1")
    fun getRecentCatFactEntity(): Flow<CatFactEntity?>
}