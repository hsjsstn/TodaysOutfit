package ddwu.com.mobile.project.todaysoutfit.data

import DiaryWithWeather
import androidx.room.*

@Dao
interface DiaryDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiary(diary: DiaryEntryEntity)

    @Update
    suspend fun updateDiary(diary: DiaryEntryEntity)

    @Delete
    suspend fun deleteDiary(diary: DiaryEntryEntity)

    @Query("SELECT * FROM diary_entries WHERE weatherDate = :weatherDate")
    suspend fun getDiaryByWeatherDate(weatherDate: String): List<DiaryEntryEntity>

    @Query("SELECT * FROM diary_entries")
    suspend fun getAllDiaries(): List<DiaryEntryEntity>

    @Transaction
    @Query("SELECT * FROM diary_entries WHERE id = :diaryId")
    suspend fun getDiaryWithWeather(diaryId: Int): DiaryWithWeather
}