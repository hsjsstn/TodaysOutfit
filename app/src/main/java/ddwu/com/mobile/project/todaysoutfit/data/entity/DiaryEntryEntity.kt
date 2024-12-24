package ddwu.com.mobile.project.todaysoutfit.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diary_entries")
data class DiaryEntryEntity(
    @PrimaryKey val date: String,
    var location: String?,
    var maxTemperature: Int?,
    var minTemperature: Int?,
    var top: String?,
    var bottom: String?,
    var outer: String?,
    var accessory: String?,
    var satisfaction: String?,
    var memo: String?
)