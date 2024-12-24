package ddwu.com.mobile.project.todaysoutfit.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ddwu.com.mobile.project.todaysoutfit.data.entity.WeatherEntity

@Dao
interface WeatherDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherEntity)

    @Query("SELECT * FROM weather WHERE date = :date AND location = :location")
    suspend fun getWeather(date: String, location: String): WeatherEntity?

    @Query("SELECT * FROM weather")
    suspend fun getAllWeather(): List<WeatherEntity>
}