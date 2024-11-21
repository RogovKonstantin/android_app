package utils.db

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import utils.HeroMapper
import utils.HeroModel


class HeroDbRepository(private val heroDao: HeroDao) {

    suspend fun saveHeroesToLocal(heroes: List<HeroModel>) = withContext(Dispatchers.IO) {
        val heroEntities = heroes.map(HeroMapper::toEntity)
        heroDao.insertHeroes(heroEntities)
    }

    fun fetchHeroesFromLocal(): Flow<List<HeroModel>> {
        return heroDao.getAllHeroes().map { entities ->
            entities.map(HeroMapper::toModel)
        }
    }

    suspend fun deleteHero(hero: HeroModel) = withContext(Dispatchers.IO) {
        heroDao.deleteHero(HeroMapper.toEntity(hero))
    }
}

