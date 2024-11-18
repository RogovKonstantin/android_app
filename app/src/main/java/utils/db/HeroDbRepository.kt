package utils.db

import androidx.lifecycle.LiveData

class HeroDbRepository(private val heroDao: HeroDao) {

   fun saveHeroes(heroes: List<HeroEntity>) {
        heroDao.insertHeroes(heroes)
    }

    fun getHeroes(): LiveData<List<HeroEntity>> {
        return heroDao.getAllHeroes()
    }

    fun deleteAllHeroes() {
        heroDao.deleteAllHeroes()
    }
    fun getHeroesDirectly(): List<HeroEntity> {
        return heroDao.getAllHeroesDirectly()
    }

}