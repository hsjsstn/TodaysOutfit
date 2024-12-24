package ddwu.com.mobile.project.todaysoutfit.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import ddwu.com.mobile.project.todaysoutfit.R
import ddwu.com.mobile.project.todaysoutfit.ui.activity.AddDiaryEntryActivity

class DiaryCalendarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_calendar)

        val addDiaryEntityBtn = findViewById<Button>(R.id.addDiaryEntityBtn)
        addDiaryEntityBtn.setOnClickListener {
            val intent = Intent(this, AddDiaryEntryActivity::class.java)
            startActivity(intent)
        }
    }
}