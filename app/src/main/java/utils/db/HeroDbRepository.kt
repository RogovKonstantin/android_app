package utils.db

class HeroDbRepository(private val heroDao: HeroDao) {
    suspend fun saveHeroes(heroes: List<HeroEntity>) {
        heroDao.insertHeroes(heroes)
    }

    suspend fun getHeroes(): List<HeroEntity> {
        return heroDao.getAllHeroes()
    }

    suspend fun deleteAllHeroes() {
        heroDao.deleteAllHeroes()
    }

}