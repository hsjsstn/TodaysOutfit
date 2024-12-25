package ddwu.com.mobile.project.todaysoutfit.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ddwu.com.mobile.project.todaysoutfit.data.entity.DiaryEntryEntity
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.Delete

@Dao
interface DiaryDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiary(diary: DiaryEntryEntity)

    @Update
    suspend fun updateDiary(diary: DiaryEntryEntity)

    @Delete
    suspend fun deleteDiary(diary: DiaryEntryEntity)

    @Query("SELECT * FROM diary_entries WHERE id = :id")
    suspend fun getDiaryById(id: Int): DiaryEntryEntity?

    @Query("SELECT * FROM diary_entries")
    fun getAllDiaries(): LiveData<List<DiaryEntryEntity>>

    @Query("DELETE FROM diary_entries WHERE id = :id")
    suspend fun deleteDiaryById(id: Int)
}