package ddwu.com.mobile.project.todaysoutfit.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import ddwu.com.mobile.project.todaysoutfit.R
import ddwu.com.mobile.project.todaysoutfit.data.DiaryDatabase
import kotlinx.coroutines.launch
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
}