package utils.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import utils.HeroModel
import utils.db.HeroDbRepository
import utils.db.HeroEntity
import java.io.IOException

class HeroRepository(private val apiService: ApiService, private val heroDbRepository:HeroDbRepository) {

    private var _heroes: List<HeroModel>? = null

    val heroes: List<HeroModel>
        get() = _heroes ?: throw RuntimeException("No heroes found")

    suspend fun fetchHeroes(): List<HeroModel> {
        return withContext(Dispatchers.IO) {
            try {
                val heroList = apiService.getHeroes()
                _heroes = heroList
                heroList
            } catch (e: HttpException) {
                throw Exception("Server error: ${e.message()}")
            } catch (e: IOException) {
                throw Exception("Network error")
            }
        }
    }
    suspend fun saveHeroesToLocal(heroes: List<HeroModel>) {
        val heroEntities = heroes.map { hero ->
            HeroEntity(
                id = hero.id,
                firstName = hero.firstName,
                lastName = hero.lastName,
                title = hero.title,
                family = hero.family,
                imageUrl = hero.imageUrl
            )
        }
        heroDbRepository.saveHeroes(heroEntities)
    }

    suspend fun fetchHeroesFromLocal(): List<HeroEntity> {
        return heroDbRepository.getHeroes()
    }
}
