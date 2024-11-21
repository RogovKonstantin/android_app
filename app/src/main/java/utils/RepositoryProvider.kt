package utils

import android.content.Context
import utils.api.ApiService
import utils.api.HeroApiRepository
import utils.db.AppDb
import utils.db.HeroDbRepository

object RepositoryProvider {
    fun provideHeroDbRepository(context: Context): HeroDbRepository {
        val db = AppDb.getDatabase(context)
        val heroDao = db.heroDao()
        return HeroDbRepository(heroDao)
    }

    fun provideHeroApiRepository(apiService: ApiService): HeroApiRepository {
        return HeroApiRepository(apiService)
    }
}