package ddwu.com.mobile.project.todaysoutfit.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ddwu.com.mobile.project.todaysoutfit.data.dao.DiaryDAO
import ddwu.com.mobile.project.todaysoutfit.data.dao.WeatherDAO
import ddwu.com.mobile.project.todaysoutfit.data.entity.DiaryEntryEntity
import ddwu.com.mobile.project.todaysoutfit.data.entity.WeatherEntity

@Database(entities = [DiaryEntryEntity::class, WeatherEntity::class], version = 1, exportSchema = false)
abstract class DiaryDatabase : RoomDatabase() {

    abstract fun diaryDAO(): DiaryDAO
    abstract fun weatherDAO(): WeatherDAO

    companion object {
        @Volatile
        private var INSTANCE: DiaryDatabase? = null

        fun getDatabase(context: Context): DiaryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DiaryDatabase::class.java,
                    "diary_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}