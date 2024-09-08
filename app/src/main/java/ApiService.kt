import retrofit2.http.GET

interface ApiService {
    @GET("https://thronesapi.com/api/v2/Characters")
    suspend fun getUsers(): List<HeroModel>
}