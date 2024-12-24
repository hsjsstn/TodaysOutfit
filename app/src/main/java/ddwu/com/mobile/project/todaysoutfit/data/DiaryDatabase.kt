package ddwu.com.mobile.project.todaysoutfit.data

object DiaryDatabase {

    private val diaryEntries = mutableListOf<DiaryEntryDTO>()

    fun addDiaryEntry(entry: DiaryEntryDTO) {
        diaryEntries.add(entry)
    }

    fun updateDiaryEntry(date: String, updatedEntry: DiaryEntryDTO) {
        diaryEntries.find { it.date == date }?.apply {
            location = updatedEntry.location
            maxTemperature = updatedEntry.maxTemperature
            minTemperature = updatedEntry.minTemperature
            top = updatedEntry.top
            bottom = updatedEntry.bottom
            outer = updatedEntry.outer
            accessory = updatedEntry.accessory
            satisfaction = updatedEntry.satisfaction
            memo = updatedEntry.memo
        }
    }

    fun deleteDiaryEntry(date: String) {
        diaryEntries.removeIf { it.date == date }
    }

    fun getDiaryEntry(date: String): DiaryEntryDTO? {
        return diaryEntries.find { it.date == date }
    }

    fun getAllDiaryEntries(): List<DiaryEntryDTO> {
        return diaryEntries.toList()
    }
}