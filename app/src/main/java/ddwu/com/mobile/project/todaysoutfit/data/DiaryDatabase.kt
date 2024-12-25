package ddwu.com.mobile.project.todaysoutfit.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import ddwu.com.mobile.project.todaysoutfit.data.dao.DiaryDAO
import ddwu.com.mobile.project.todaysoutfit.data.entity.DiaryEntryEntity

@Database(entities = [DiaryEntryEntity::class], version = 2, exportSchema = false)
abstract class DiaryDatabase : RoomDatabase() {

    abstract fun diaryDAO(): DiaryDAO

    companion object {
        @Volatile
        private var INSTANCE: DiaryDatabase? = null

        fun getDatabase(context: Context): DiaryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DiaryDatabase::class.java,
                    "diary_database"
                )
                    //.fallbackToDestructiveMigration()
                    .addMigrations(MIGRATION_1_2) // 마이그레이션 추가
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
private val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // 새 테이블 생성
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS diary_entries_new (" +
                    "id INTEGER NOT NULL, " +
                    "date TEXT NOT NULL, " +
                    "location TEXT, " +
                    "top TEXT, " +
                    "bottom TEXT, " +
                    "outer TEXT, " +
                    "accessory TEXT, " +
                    "satisfaction TEXT, " +
                    "memo TEXT, " +
                    "PRIMARY KEY(id))"
        )

        // 기존 데이터 복사
        database.execSQL(
            "INSERT INTO diary_entries_new (id, date, location, top, bottom, outer, accessory, satisfaction, memo) " +
                    "SELECT id, date, location, top, bottom, outer, accessory, satisfaction, memo FROM diary_entries"
        )

        // 기존 테이블 삭제
        database.execSQL("DROP TABLE diary_entries")

        // 새 테이블 이름 변경
        database.execSQL("ALTER TABLE diary_entries_new RENAME TO diary_entries")
    }
}