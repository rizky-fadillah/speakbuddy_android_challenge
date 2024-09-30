package jp.speakbuddy.edisonandroidexercise.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import jp.speakbuddy.edisonandroidexercise.data.local.dao.CatFactDao
import jp.speakbuddy.edisonandroidexercise.data.local.model.CatFactEntity

@Database(entities = [CatFactEntity::class], version = 1)
abstract class CatFactDatabase : RoomDatabase() {
    abstract fun catFactDao(): CatFactDao
}