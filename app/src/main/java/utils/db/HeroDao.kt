package utils.db


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import kotlinx.coroutines.flow.Flow

@Dao
interface HeroDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHeroes(heroes: List<HeroEntity>): List<Long>

    @Query("SELECT * FROM heroes")
    @RewriteQueriesToDropUnusedColumns
    fun getAllHeroes(): Flow<List<HeroEntity>>

    @Delete
    fun deleteHero(hero: HeroEntity): Int

    @Query("DELETE FROM heroes")
    fun deleteAllHeroes(): Int

}
