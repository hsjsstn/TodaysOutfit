package ddwu.com.mobile.project.todaysoutfit.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import ddwu.com.mobile.project.todaysoutfit.R
import ddwu.com.mobile.project.todaysoutfit.data.DiaryDatabase
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import android.app.AlertDialog
import android.util.Log
import android.widget.ImageButton


class DiaryCalendarActivity : AppCompatActivity() {
    private lateinit var calendarView: CalendarView
    private lateinit var selectedDateTextView: TextView
    private var selectedDate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_calendar)

        // CalendarView와 TextView 연결
        calendarView = findViewById(R.id.calendarView)
        selectedDateTextView = findViewById(R.id.selectedDateTextView)

        // 현재 날짜로 초기화
        val calendar = Calendar.getInstance()
        val formattedDate = SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault()).format(calendar.time)
        selectedDate = formattedDate
        selectedDateTextView.text = formattedDate

        // CalendarView의 날짜도 현재 날짜로 초기화
        calendarView.date = calendar.timeInMillis

        // 날짜 선택 이벤트
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // 선택된 날짜 형식 설정
            selectedDate = String.format("%04d년 %02d월 %02d일", year, month + 1, dayOfMonth)
            selectedDateTextView.text = selectedDate // TextView에 표시
        }

        val goToHomeButton = findViewById<ImageButton>(R.id.goToHome)
        goToHomeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // 옷차림 보기 버튼 클릭 이벤트
        findViewById<Button>(R.id.viewDiaryEntityBtn).setOnClickListener {
            val date = selectedDate
            if (date != null) {
                val id = calculateDiaryId(date)
                checkDiaryExistsAndOpen(id)
            } else {
                showAlert("날짜를 선택해주세요.")
            }
        }

        // 작성하기 버튼 클릭 이벤트
        findViewById<Button>(R.id.addDiaryEntityBtn).setOnClickListener {
            val date = selectedDate
            if (date != null) {
                val id = calculateDiaryId(date)
                checkDiaryExistsBeforeAdding(id)
            } else {
                showAlert("날짜를 선택해주세요.")
            }
        }
    }

    // 날짜를 기반으로 Diary ID 계산
    private fun calculateDiaryId(date: String): Int {
        try {
            val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault())
            val parsedDate = dateFormat.parse(date) ?: throw IllegalArgumentException("날짜 파싱 실패: $date")
            val idFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
            return idFormat.format(parsedDate).toInt()
        } catch (e: Exception) {
            e.printStackTrace()
            throw IllegalArgumentException("Diary ID 계산 중 오류 발생: ${e.message}")
        }
    }

    // Diary 존재 여부를 확인하고 이동 또는 알림
    private fun checkDiaryExistsAndOpen(diaryId: Int) {
        lifecycleScope.launch {
            try {
                val diaryDAO = DiaryDatabase.getDatabase(this@DiaryCalendarActivity).diaryDAO()
                Log.d("DiaryDAO", "Fetching diary with id: $diaryId")
                val diary = diaryDAO.getDiaryById(diaryId)
                Log.d("DiaryDAO", "Fetched diary: $diary")

                if (diary != null) {
                    val intent = Intent(this@DiaryCalendarActivity, DiaryDetailActivity::class.java)
                    intent.putExtra("diaryId", diaryId)
                    startActivity(intent)
                } else {
                    showAlert("해당 날짜에 기록된 옷차림이 없습니다.")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                showAlert("데이터를 가져오는 중 오류 발생: ${e.message}")
            }
        }
    }

    // Diary 존재 여부를 확인하고 작성 방지 또는 이동
    private fun checkDiaryExistsBeforeAdding(diaryId: Int) {
        lifecycleScope.launch {
            val diaryDAO = DiaryDatabase.getDatabase(this@DiaryCalendarActivity).diaryDAO()
            val diary = diaryDAO.getDiaryById(diaryId)

            if (diary != null) {
                // 이미 저장된 다이어리가 있을 경우 알림 표시
                showAlert("해당 날짜에 저장된 옷차림이 있습니다.")
            } else {
                // 작성하기 액티비티로 이동
                val intent = Intent(this@DiaryCalendarActivity, AddDiaryEntryActivity::class.java)
                intent.putExtra("selectedDate", selectedDate) // 선택된 날짜 전달
                startActivity(intent)
            }
        }
    }

    // AlertDialog를 표시하는 함수
    private fun showAlert(message: String) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("확인") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}