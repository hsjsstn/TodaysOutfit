package ddwu.com.mobile.project.todaysoutfit.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import ddwu.com.mobile.project.todaysoutfit.R
import ddwu.com.mobile.project.todaysoutfit.data.DiaryDatabase
import ddwu.com.mobile.project.todaysoutfit.data.network.RetrofitInstance
import ddwu.com.mobile.project.todaysoutfit.data.network.WeatherResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var recmdDate1: TextView
    private lateinit var recmdDate2: TextView
    private lateinit var recmdDate3: TextView

    private lateinit var lastOutfit1: TextView
    private lateinit var lastOutfit2: TextView
    private lateinit var lastOutfit3: TextView

    private lateinit var lastSatisfaction1: TextView
    private lateinit var lastSatisfaction2: TextView
    private lateinit var lastSatisfaction3: TextView

    private val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 초단기 실황 데이터 가져오기
        fetchCurrentWeather()

        // 단기 예보 데이터 가져오기
        fetchForecastWeather()

        // View 연결
        recmdDate1 = findViewById(R.id.recmdDate1)
        recmdDate2 = findViewById(R.id.recmdDate2)
        recmdDate3 = findViewById(R.id.recmdDate3)

        lastOutfit1 = findViewById(R.id.lastOutfit1)
        lastOutfit2 = findViewById(R.id.lastOutfit2)
        lastOutfit3 = findViewById(R.id.lastOutfit3)

        lastSatisfaction1 = findViewById(R.id.lastSatisfaction1)
        lastSatisfaction2 = findViewById(R.id.lastSatisfaction2)
        lastSatisfaction3 = findViewById(R.id.lastSatisfaction3)

        // 오늘 기준 날짜와 데이터 로드
        loadOutfits()

        val goToCalendarButton = findViewById<ImageButton>(R.id.goToCalendar)
        goToCalendarButton.setOnClickListener {
            val intent = Intent(this, DiaryCalendarActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadOutfits() {
        lifecycleScope.launch {
            val diaryDAO = DiaryDatabase.getDatabase(this@MainActivity).diaryDAO()
            val calendar = Calendar.getInstance()

            for (i in 1..3) {
                calendar.add(Calendar.DAY_OF_YEAR, -1) // 하루씩 감소
                val dateString = dateFormat.format(calendar.time)

                when (i) {
                    1 -> updateDiaryInfo(diaryDAO, dateString, recmdDate1, lastOutfit1, lastSatisfaction1)
                    2 -> updateDiaryInfo(diaryDAO, dateString, recmdDate2, lastOutfit2, lastSatisfaction2)
                    3 -> updateDiaryInfo(diaryDAO, dateString, recmdDate3, lastOutfit3, lastSatisfaction3)
                }
            }
        }
    }

    private suspend fun updateDiaryInfo(
        diaryDAO: ddwu.com.mobile.project.todaysoutfit.data.dao.DiaryDAO,
        date: String,
        dateView: TextView,
        outfitView: TextView,
        satisfactionView: TextView
    ) {
        val diaryId = calculateDiaryId(date)
        val diaryEntry = diaryDAO.getDiaryById(diaryId)

        dateView.text = date // 날짜 출력
        if (diaryEntry != null) {
            val outfit = buildString {
                appendLine(diaryEntry.top)
                appendLine(diaryEntry.bottom)
                diaryEntry.outer?.let { appendLine(it) }
                diaryEntry.accessory?.let { appendLine(it) }
            }.trim() // 줄바꿈과 함께 데이터 정리
            outfitView.text = outfit

            // 만족도 출력
            satisfactionView.text = diaryEntry.satisfaction ?: ""
        } else {
            outfitView.text = "옷차림 정보 없음"
            satisfactionView.text = ""
        }
    }

    private fun calculateDiaryId(date: String): Int {
        val parsedDate = dateFormat.parse(date) ?: throw IllegalArgumentException("날짜 형식이 잘못되었습니다.")
        val idFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        return idFormat.format(parsedDate).toInt()
    }

    // 초단기 실황
    private fun fetchCurrentWeather() {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HHmm", Locale.getDefault())

        val baseDate = dateFormat.format(calendar.time)
        val baseTime = timeFormat.format(calendar.time)

        val call = RetrofitInstance.api.getCurrentWeather(
            serviceKey = getString(R.string.weather_key),
            baseDate = baseDate,
            baseTime = baseTime,
            nx = 60,
            ny = 127
        )

        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    val items = response.body()?.response?.body?.items?.item
                    val currentTemperature = items?.find { it.category == "T1H" }?.obsrValue

                    findViewById<TextView>(R.id.currentTemp).text =
                        currentTemperature?.let { "$it°C" } ?: "기온 정보를 찾을 수 없습니다."
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                findViewById<TextView>(R.id.currentTemp).text = "네트워크 오류 발생"
            }
        })
    }

    // 단기 예보
    private fun fetchForecastWeather() {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())

        val baseDate = dateFormat.format(calendar.time)
        val baseTime = "0500" // 단기 예보의 base_time은 보통 고정된 값 (0200, 0500 등)

        val call = RetrofitInstance.api.getForecastWeather(
            serviceKey = getString(R.string.weather_key),
            baseDate = baseDate,
            baseTime = baseTime,
            nx = 60,
            ny = 127
        )

        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    val items = response.body()?.response?.body?.items?.item
                    Log.d("WeatherAPI", "API Response Items: $items")

                    val maxTemp = items?.find { it.category == "TMX" }?.fcstValue
                    val minTemp = items?.find { it.category == "TMN" }?.fcstValue

                    findViewById<TextView>(R.id.maxMinTemp).text =
                        if (maxTemp != null && minTemp != null) {
                            "$maxTemp°C / $minTemp°C"
                        } else {
                            "최고/최저 기온 정보를 찾을 수 없습니다."
                        }

                    // SKY와 PTY 데이터 가져오기
                    val sky = items?.find { it.category == "SKY" }?.fcstValue?.toIntOrNull()
                    val pty = items?.find { it.category == "PTY" }?.fcstValue?.toIntOrNull()

                    if (sky != null && pty != null) {
                        Log.d("WeatherImage", "SKY: $sky, PTY: $pty")
                        updateWeatherImage(sky, pty)
                    } else {
                        Log.d("WeatherImage", "SKY 또는 PTY 정보를 찾을 수 없습니다. 데이터 확인 필요.")
                    }
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                findViewById<TextView>(R.id.maxMinTemp).text = "네트워크 오류 발생"
            }
        })
    }

    private fun updateWeatherImage(sky: Int, pty: Int) {
        val weatherImg = findViewById<ImageView>(R.id.weatherImg)

        // PTY 우선 판단 (강수 상태)
        when (pty) {
            1, 2, 4, 5, 6 -> {
                // 비
                weatherImg.setImageResource(R.drawable.weather_rain)
                return
            }
            3, 7 -> {
                // 눈
                weatherImg.setImageResource(R.drawable.weather_snow)
                return
            }
        }

        // SKY 판단 (하늘 상태)
        when (sky) {
            1 -> {
                // 맑음
                weatherImg.setImageResource(R.drawable.weather_sun)
            }
            3, 4 -> {
                // 구름 많음 또는 흐림
                weatherImg.setImageResource(R.drawable.weather_cloud)
            }
            else -> {
                // 기본 이미지 (맑음 이미지)
                weatherImg.setImageResource(R.drawable.weather_sun)
            }
        }
    }
}