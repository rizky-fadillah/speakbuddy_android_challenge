package jp.speakbuddy.edisonandroidexercise.data.local.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import jp.speakbuddy.edisonandroidexercise.model.Fact

@Entity(
    indices = [
        Index(value = ["fact"], unique = true)
    ]
)
data class CatFactEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val fact: String,
    val length: Int
)

fun CatFactEntity.asExternalModel() = Fact(
    fact = fact,
    length = length,
)