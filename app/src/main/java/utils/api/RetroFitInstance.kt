package utils.api

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import utils.db.AppDb
import utils.db.HeroDbRepository

object RetrofitInstance {
    private const val BASE_URL = "https://thronesapi.com/api/v2/"
    private val contentType = "application/json".toMediaType()
    private val json = Json { ignoreUnknownKeys = true }
    @OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(json.asConverterFactory(contentType))
        .build()
    private val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
    fun provideHeroRepository(context: Context): HeroRepository {
        val db = AppDb.getDatabase(context)
        val heroDao = db.heroDao()
        val heroDbRepository = HeroDbRepository(heroDao)
        return HeroRepository(api, heroDbRepository)
    }
}