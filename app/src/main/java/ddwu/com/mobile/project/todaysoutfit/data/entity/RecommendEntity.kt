package ddwu.com.mobile.project.todaysoutfit.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recommendations")
data class RecommendEntity(
    @PrimaryKey val temperatureRange: String, // 예: "30+", "20-25"
    val clothingSuggestion: String // 예: "반팔, 반바지"
)