package ddwu.com.mobile.project.todaysoutfit.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ddwu.com.mobile.project.todaysoutfit.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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

        // 작성하기 버튼 클릭 이벤트
        findViewById<Button>(R.id.addDiaryEntityBtn).setOnClickListener {
            val intent = Intent(this, AddDiaryEntryActivity::class.java)
            intent.putExtra("selectedDate", selectedDate) // 선택된 날짜 전달
            startActivity(intent)
        }
    }
}