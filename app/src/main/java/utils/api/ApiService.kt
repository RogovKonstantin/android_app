package utils.api

import retrofit2.http.GET
import utils.HeroModel

interface ApiService {
    @GET("Characters")
    suspend fun getUsers(): List<HeroModel>
}