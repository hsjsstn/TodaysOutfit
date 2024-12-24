package ddwu.com.mobile.project.todaysoutfit.data

import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

object WeatherDatabase {

    fun getTodayWeather(location: String): WeatherDTO {
        val weatherApiUrl = "https://api.weather.go.kr/...&location=$location" // API URL 수정 필요
        val jsonResponse = fetchWeatherData(weatherApiUrl)

        return parseWeatherData(jsonResponse)
    }

    private fun fetchWeatherData(apiUrl: String): String {
        val url = URL(apiUrl)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connect()

        val inputStream = connection.inputStream.bufferedReader()
        return inputStream.use { it.readText() }
    }

    private fun parseWeatherData(jsonResponse: String): WeatherDTO {
        val jsonObject = JSONObject(jsonResponse)

        return WeatherDTO().apply {
            temperature = jsonObject.getJSONObject("main").getDouble("temp")
            maxTemperature = jsonObject.getJSONObject("main").getDouble("temp_max")
            minTemperature = jsonObject.getJSONObject("main").getDouble("temp_min")
            condition = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description")
        }
    }
}