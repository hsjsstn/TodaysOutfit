package ddwu.com.mobile.project.todaysoutfit.data.dao

import androidx.room.*
import ddwu.com.mobile.project.todaysoutfit.data.entity.DiaryEntryEntity

@Dao
interface DiaryDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiary(diary: DiaryEntryEntity)

    @Update
    suspend fun updateDiary(diary: DiaryEntryEntity)

    @Delete
    suspend fun deleteDiary(diary: DiaryEntryEntity)

    @Query("SELECT * FROM diary_entries WHERE date = :weatherDate")
    suspend fun getDiaryByWeatherDate(weatherDate: String): List<DiaryEntryEntity>

    @Query("SELECT * FROM diary_entries")
    suspend fun getAllDiaries(): List<DiaryEntryEntity>
}