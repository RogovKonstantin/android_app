package utils.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "heroes")
data class HeroEntity(
    @PrimaryKey val id: Int,
    val firstName: String,
    val lastName: String,
    val fullName: String,
    val title: String,
    val family: String,
    val image: String,
    val imageUrl: String,
)