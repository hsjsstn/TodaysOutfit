import androidx.room.Embedded
import androidx.room.Relation
import ddwu.com.mobile.project.todaysoutfit.data.DiaryEntity
import ddwu.com.mobile.project.todaysoutfit.data.entity.WeatherEntity

data class DiaryWithWeather(
    @Embedded val diary: DiaryEntity,
    @Relation(
        parentColumn = "weatherDate",
        entityColumn = "date"
    )
    val weather: WeatherEntity
)