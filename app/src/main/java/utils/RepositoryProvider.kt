package utils

import android.content.Context
import utils.api.ApiService
import utils.api.HeroApiRepository
import utils.db.AppDb
import utils.db.HeroDbRepository

object RepositoryProvider {
    private var db: AppDb? = null

    fun provideHeroDbRepository(context: Context): HeroDbRepository {
        if (db == null) {
            db = AppDb.getDatabase(context)
        }
        return HeroDbRepository(db!!.heroDao())
    }

    fun provideHeroApiRepository(apiService: ApiService): HeroApiRepository {
        return HeroApiRepository(apiService)
    }
}
