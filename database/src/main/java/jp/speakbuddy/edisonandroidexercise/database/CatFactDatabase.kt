package jp.speakbuddy.edisonandroidexercise.database

import androidx.room.Database
import androidx.room.RoomDatabase
import jp.speakbuddy.edisonandroidexercise.database.dao.CatFactDao
import jp.speakbuddy.edisonandroidexercise.database.model.CatFactEntity

@Database(entities = [CatFactEntity::class], version = 1)
abstract class CatFactDatabase : RoomDatabase() {
    abstract fun catFactDao(): CatFactDao
}