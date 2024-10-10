package utils.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import utils.HeroModel
import java.io.IOException

class HeroRepository(private val apiService: ApiService) {

    private var _heroes: List<HeroModel>? = null

    val heroes: List<HeroModel>
        get() = _heroes ?: throw RuntimeException("No heroes found")

    suspend fun fetchHeroes(): List<HeroModel> {
        return withContext(Dispatchers.IO) {
            try {
                val heroList = apiService.getUsers()
                _heroes = heroList
                heroList
            } catch (e: HttpException) {
                throw Exception("Server error: ${e.message()}")
            } catch (e: IOException) {
                throw Exception("Network error")
            }
        }
    }
}
