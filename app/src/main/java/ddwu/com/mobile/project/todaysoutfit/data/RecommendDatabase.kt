package ddwu.com.mobile.project.todaysoutfit.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ddwu.com.mobile.project.todaysoutfit.data.dao.RecommendDAO
import ddwu.com.mobile.project.todaysoutfit.data.entity.RecommendEntity

@Database(entities = [RecommendEntity::class], version = 1, exportSchema = false)
abstract class RecommendDatabase : RoomDatabase() {

    abstract fun recommendDAO(): RecommendDAO

    companion object {
        @Volatile
        private var INSTANCE: RecommendDatabase? = null

        fun getDatabase(context: Context): RecommendDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RecommendDatabase::class.java,
                    "recommend_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}