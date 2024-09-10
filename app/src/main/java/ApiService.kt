import retrofit2.http.GET

interface ApiService {
    @GET("Characters")
    suspend fun getUsers(): List<HeroModel>
}