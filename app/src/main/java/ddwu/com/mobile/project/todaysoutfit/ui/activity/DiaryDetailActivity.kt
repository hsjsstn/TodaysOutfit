package ddwu.com.mobile.project.todaysoutfit.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ddwu.com.mobile.project.todaysoutfit.R
import ddwu.com.mobile.project.todaysoutfit.data.viewmodel.DiaryViewModel
import kotlinx.coroutines.launch

class DiaryDetailActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var diaryViewModel: DiaryViewModel
    private lateinit var googleMap: GoogleMap

    // UI 요소 선언
    private lateinit var selectDate: TextView
    private lateinit var satisfaction: TextView
    private lateinit var address: TextView
    private lateinit var topOutfit: TextView
    private lateinit var bottomOutfit: TextView
    private lateinit var outerOutfit: TextView
    private lateinit var accessories: TextView
    private lateinit var memo: TextView
    private lateinit var editButton: Button
    private lateinit var deleteButton: Button


    private var diaryId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_detail)

        // ViewModel 초기화
        diaryViewModel = ViewModelProvider(this).get(DiaryViewModel::class.java)

        // UI 초기화
        selectDate = findViewById(R.id.selectDate)
        address = findViewById(R.id.address)
        satisfaction = findViewById(R.id.satisfaction)
        topOutfit = findViewById(R.id.topOutfit)
        bottomOutfit = findViewById(R.id.bottomOutfit)
        outerOutfit = findViewById(R.id.outerOutfit)
        accessories = findViewById(R.id.accessories)
        memo = findViewById(R.id.memo)
        editButton = findViewById(R.id.editButton)
        deleteButton = findViewById(R.id.deleteButton)

        // Intent로 전달된 Diary ID
        diaryId = intent.getIntExtra("diaryId", -1)
        if (diaryId != -1) {
            loadDiaryDetails(diaryId)
        } else {
            showError("Diary ID를 찾을 수 없습니다.")
        }
        // 지도 준비
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // 수정 버튼 클릭
        editButton.setOnClickListener {
            val intent = Intent(this, AddDiaryEntryActivity::class.java).apply {
                putExtra("DIARY_ID", diaryId)
            }
            startActivity(intent)
        }

        // 삭제 버튼 클릭
        deleteButton.setOnClickListener {
            diaryViewModel.deleteDiaryById(diaryId)
            showSuccess("삭제되었습니다.")
            finish()
        }
    }

    private fun loadDiaryDetails(id: Int) {
        lifecycleScope.launch {
            val diary = diaryViewModel.getDiaryById(id)
            if (diary != null) {
                selectDate.text = diary.date
                address.text = diary.location
                satisfaction.text = diary.satisfaction
                topOutfit.text = diary.top
                bottomOutfit.text = diary.bottom
                outerOutfit.text = diary.outer
                accessories.text = diary.accessory
                memo.text = diary.memo

                // diary.location을 불변 변수에 저장
                val locationString = diary.location
                if (!locationString.isNullOrEmpty()) {
                    val location = locationString.split(",").map { it.trim().toDoubleOrNull() }
                    if (location.size == 2 && location[0] != null && location[1] != null) {
                        updateMap(LatLng(location[0]!!, location[1]!!), locationString)
                    }
                }
            } else {
                showError("저장된 다이어리 정보를 찾을 수 없습니다.")
            }
        }
    }

    private fun updateMap(location: LatLng, placeName: String) {
        googleMap.clear()
        googleMap.addMarker(MarkerOptions().position(location).title(placeName))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
        googleMap.uiSettings.isScrollGesturesEnabled = false
        googleMap.uiSettings.isZoomGesturesEnabled = false
    }

    private fun deleteDiaryEntry(id: Int) {
        diaryViewModel.deleteDiaryById(id)
        showSuccess("삭제되었습니다.")
        finish()
    }

    private fun showError(message: String) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_LONG).show()
    }

    private fun showSuccess(message: String) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
    }
}