package utils.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns

@Dao
interface HeroDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHeroes(heroes: List<HeroEntity>): List<Long>

    @Query("SELECT * FROM heroes")
    @RewriteQueriesToDropUnusedColumns
    fun getAllHeroes(): LiveData<List<HeroEntity>>

    @Delete
    fun deleteHero(hero: HeroEntity): Int

    @Query("DELETE FROM heroes")
    fun deleteAllHeroes(): Int

    @Query("SELECT * FROM heroes")
    fun getAllHeroesDirectly(): List<HeroEntity>
}
