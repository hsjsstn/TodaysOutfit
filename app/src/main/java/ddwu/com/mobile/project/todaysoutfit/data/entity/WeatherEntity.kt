package ddwu.com.mobile.project.todaysoutfit.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey val date: String, // 날짜
    val location: String, // 지역명
    val temperature: Double, // 현재 기온
    val maxTemperature: Double, // 최고 기온
    val minTemperature: Double, // 최저 기온
    val condition: String // 날씨 상태 (예: 맑음, 흐림)
)