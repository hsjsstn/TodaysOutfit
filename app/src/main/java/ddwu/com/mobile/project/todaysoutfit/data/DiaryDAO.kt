import ddwu.com.mobile.project.todaysoutfit.data.DiaryEntryDTO

class DiaryDAO {
    private val diaryEntries: MutableList<DiaryEntryDTO> = ArrayList()

    fun addDiaryEntry(entry: DiaryEntryDTO) {
        diaryEntries.add(entry)
    }

    fun updateDiaryEntry(date: String, updatedEntry: DiaryEntryDTO) {
        for (entry in diaryEntries) {
            if (entry.date == date) {
                entry.location = updatedEntry.location
                entry.top = updatedEntry.top
                entry.bottom = updatedEntry.bottom
                entry.outer = updatedEntry.outer
                entry.accessory = updatedEntry.accessory
                entry.satisfaction = updatedEntry.satisfaction
                entry.memo = updatedEntry.memo
                break
            }
        }
    }

    fun deleteDiaryEntry(date: String) {
        diaryEntries.removeIf { entry: DiaryEntryDTO -> entry.date == date }
    }

    fun getDiaryEntry(date: String): DiaryEntryDTO? {
        for (entry in diaryEntries) {
            if (entry.date == date) {
                return entry
            }
        }
        return null // 해당 날짜에 기록된 데이터가 없을 경우 null 반환
    }

    val allDiaryEntries: List<DiaryEntryDTO>
        get() = ArrayList(diaryEntries)
}