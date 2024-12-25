package ddwu.com.mobile.project.todaysoutfit.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import ddwu.com.mobile.project.todaysoutfit.data.dao.DiaryDAO
import ddwu.com.mobile.project.todaysoutfit.data.DiaryDatabase
import ddwu.com.mobile.project.todaysoutfit.data.entity.DiaryEntryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DiaryViewModel(application: Application) : AndroidViewModel(application) {

    private val diaryDAO: DiaryDAO
    val allDiaries: LiveData<List<DiaryEntryEntity>>

    init {
        val db = DiaryDatabase.getDatabase(application)
        diaryDAO = db.diaryDAO()
        allDiaries = diaryDAO.getAllDiaries()
    }

    // ID를 사용하여 Diary를 가져오기
    suspend fun getDiaryById(id: Int): DiaryEntryEntity? {
        return diaryDAO.getDiaryById(id)
    }

    // 새 Diary 추가
    fun insertDiary(diary: DiaryEntryEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            diaryDAO.insertDiary(diary)
        }
    }

    // Diary 수정
    fun updateDiary(diary: DiaryEntryEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            diaryDAO.updateDiary(diary)
        }
    }

    // Diary 삭제
    fun deleteDiaryById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            diaryDAO.deleteDiaryById(id)
        }
    }
}