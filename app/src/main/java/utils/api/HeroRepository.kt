package utils.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import utils.HeroModel
import utils.db.HeroDbRepository
import utils.db.HeroEntity

class HeroRepository(
    private val apiService: ApiService,
    private val heroDbRepository: HeroDbRepository
) {

    suspend fun fetchHeroes(): List<HeroModel> {
        return withContext(Dispatchers.IO) {
            try {
                apiService.getHeroes()
            } catch (e: Exception) {
                throw Exception("Failed to fetch heroes from API: ${e.message}")
            }
        }
    }

    suspend fun saveHeroesToLocal(heroes: List<HeroModel>) {
        withContext(Dispatchers.IO) {
            val heroEntities = heroes.map { hero ->
                HeroEntity(
                    id = hero.id,
                    firstName = hero.firstName,
                    lastName = hero.lastName,
                    fullName = hero.fullName,
                    title = hero.title,
                    family = hero.family,
                    image = hero.image,
                    imageUrl = hero.imageUrl
                )
            }
            heroDbRepository.saveHeroes(heroEntities)
        }
    }

    fun fetchHeroesFromLocal(): Flow<List<HeroModel>> {
        return heroDbRepository.getHeroes().map { entities ->
            entities.map { entity ->
                HeroModel(
                    id = entity.id,
                    firstName = entity.firstName,
                    lastName = entity.lastName,
                    fullName = entity.fullName,
                    title = entity.title,
                    family = entity.family,
                    image = entity.image,
                    imageUrl = entity.imageUrl,
                    isPlaceholder = false
                )
            }
        }
    }

}

