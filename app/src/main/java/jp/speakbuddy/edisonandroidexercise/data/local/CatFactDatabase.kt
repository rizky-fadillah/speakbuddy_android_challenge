package jp.speakbuddy.edisonandroidexercise.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CatFactEntity::class], version = 1)
abstract class CatFactDatabase : RoomDatabase() {
    abstract fun catFactDao(): CatFactDao
}