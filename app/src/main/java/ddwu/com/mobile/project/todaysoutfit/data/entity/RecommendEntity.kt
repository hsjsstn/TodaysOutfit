package ddwu.com.mobile.project.todaysoutfit.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recommendations")
data class RecommendEntity(
    @PrimaryKey val recmdId: Int,
    val minTemp: Double, // 최소 범위
    val maxTemp: Double, // 최대 범위
    val clothingSuggestion: String // 예: "반팔, 반바지"
)