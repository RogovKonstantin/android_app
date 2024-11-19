package utils.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [HeroEntity::class], version = 1, exportSchema = false)
abstract class AppDb : RoomDatabase() {
    abstract fun heroDao(): HeroDao

    companion object {
        @Volatile private var instance: AppDb? = null

        fun getDatabase(context: Context): AppDb {
            return instance ?: synchronized(this) {
                val tempInstance = instance
                if (tempInstance != null) {
                    return tempInstance
                }

                val newInstance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDb::class.java,
                    "hero_database"
                ).fallbackToDestructiveMigration()  // Add this if you want to handle schema changes
                    .build()

                instance = newInstance
                return newInstance
            }
        }
    }
}