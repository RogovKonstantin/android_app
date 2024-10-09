package utils.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://thronesapi.com/api/v2/"
    private val contentType = "application/json".toMediaType()

    private val json = Json { ignoreUnknownKeys = true }

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(json.asConverterFactory(contentType))
        .build()

    val api: ApiService = retrofit.create(ApiService::class.java)
}