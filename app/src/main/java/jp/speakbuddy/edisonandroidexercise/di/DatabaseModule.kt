package jp.speakbuddy.edisonandroidexercise.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import jp.speakbuddy.edisonandroidexercise.data.local.CatFactDatabase

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    @Provides
    @Singleton
    fun providesCatFactDatabase(
        @ApplicationContext context: Context,
    ): CatFactDatabase = Room.databaseBuilder(
        context,
        CatFactDatabase::class.java,
        "cat-fact-database",
    ).build()
}
