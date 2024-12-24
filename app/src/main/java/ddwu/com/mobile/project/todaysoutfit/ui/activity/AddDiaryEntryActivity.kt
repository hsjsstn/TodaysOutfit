package ddwu.com.mobile.project.todaysoutfit.ui.activity

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ddwu.com.mobile.project.todaysoutfit.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddDiaryEntryActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var satisfactionDropdown: AutoCompleteTextView
    private lateinit var saveButton: Button
    private lateinit var topOutfitInput: EditText
    private lateinit var bottomOutfitInput: EditText
    private lateinit var outerOutfitInput: EditText
    private lateinit var accessoriesInput: EditText
    private lateinit var memoInput: EditText
    private var selectedLocation: LatLng? = null
    private lateinit var dateEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_diary_entry)

        // Initialize UI components
        satisfactionDropdown = findViewById(R.id.satisfaction)
        saveButton = findViewById(R.id.saveButton)
        topOutfitInput = findViewById(R.id.topOutfit)
        bottomOutfitInput = findViewById(R.id.bottomOutfit)
        outerOutfitInput = findViewById(R.id.outerOutfit)
        accessoriesInput = findViewById(R.id.accessories)
        memoInput = findViewById(R.id.memo)

        // Set up satisfaction dropdown menu
        val satisfactionOptions = listOf("Good", "Not Bad", "Cold", "Hot")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, satisfactionOptions)
        satisfactionDropdown.setAdapter(adapter)

        // Set up Google Map
        val mapFragment = supportFragmentManager.findFragmentById(R.id.googleMap) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Save button logic
        saveButton.setOnClickListener {
            val topOutfit = topOutfitInput.text.toString()
            val bottomOutfit = bottomOutfitInput.text.toString()
            val outerOutfit = outerOutfitInput.text.toString()
            val accessories = accessoriesInput.text.toString()
            val memo = memoInput.text.toString()
            val satisfaction = satisfactionDropdown.text.toString()

            // Save logic here (e.g., store in a database or send to another activity)
            if (selectedLocation != null) {
                // Save location and other data
                val location = selectedLocation!!
                // Example: Save to database
            }
        }

        // 날짜 입력 EditText
        dateEditText = findViewById(R.id.date)

        // EditText 클릭 시 달력 팝업 표시
        dateEditText.setOnClickListener {
            showDatePickerDialog()
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        val initialLocation = LatLng(-34.0, 151.0) // Default location
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, 10f))

        // Add a marker when the user selects a location
        googleMap.setOnMapClickListener { latLng ->
            googleMap.clear() // Clear previous markers
            googleMap.addMarker(MarkerOptions().position(latLng).title("Selected Location"))
            selectedLocation = latLng
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // DatePickerDialog 생성
        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                // 선택된 날짜를 Calendar에 설정
                calendar.set(selectedYear, selectedMonth, selectedDay)

                // SimpleDateFormat으로 날짜 포맷
                val formattedDate = SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault()).format(calendar.time)

                // 포맷된 날짜를 EditText에 설정
                dateEditText.setText(formattedDate)
            },
            year,
            month,
            day
        )


        datePickerDialog.show()
    }
}