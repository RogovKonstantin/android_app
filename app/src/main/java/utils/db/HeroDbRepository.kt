package utils.db

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

class HeroDbRepository(private val heroDao: HeroDao) {

   fun saveHeroes(heroes: List<HeroEntity>) {
        heroDao.insertHeroes(heroes)
    }

    fun getHeroes(): Flow<List<HeroEntity>> {
        return heroDao.getAllHeroes()
    }


    fun deleteHero(hero: HeroEntity) {
        heroDao.deleteHero(hero)
    }

}