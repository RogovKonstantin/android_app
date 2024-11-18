package utils.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "heroes")
data class HeroEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "firstName") val firstName: String,
    @ColumnInfo(name = "lastName") val lastName: String,
    @ColumnInfo(name = "fullName") val fullName: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "family") val family: String,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "imageUrl") val imageUrl: String
)