package ddwu.com.mobile.project.todaysoutfit.data.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("getUltraSrtNcst") // 초단기 실황
    fun getCurrentWeather(
        @Query("serviceKey", encoded = true) serviceKey: String,
        @Query("base_date") baseDate: String,
        @Query("base_time") baseTime: String,
        @Query("nx") nx: Int,
        @Query("ny") ny: Int,
        @Query("dataType") dataType: String = "JSON"
    ): Call<WeatherResponse>

    @GET("getVilageFcst") // 단기 예보
    fun getForecastWeather(
        @Query("serviceKey", encoded = true) serviceKey: String,
        @Query("pageNo") pageNo: Int = 1,
        @Query("numOfRows") numOfRows: Int = 500,
        @Query("dataType") dataType: String = "JSON",
        @Query("base_date") baseDate: String,
        @Query("base_time") baseTime: String,
        @Query("nx") nx: Int,
        @Query("ny") ny: Int
    ): Call<WeatherResponse>
}