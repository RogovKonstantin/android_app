package utils.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HeroDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHeroes(heroes: List<HeroEntity>)

    @Query("SELECT * FROM heroes")
    suspend fun getAllHeroes(): List<HeroEntity>

    @Delete
    suspend fun deleteHero(hero: HeroEntity)

    @Query("DELETE FROM heroes")
    suspend fun deleteAllHeroes()
}