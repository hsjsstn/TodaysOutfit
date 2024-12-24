package ddwu.com.mobile.project.todaysoutfit.ui.activity

import android.app.DatePickerDialog
import android.location.Geocoder
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import ddwu.com.mobile.project.todaysoutfit.R
import ddwu.com.mobile.project.todaysoutfit.data.DiaryDatabase
import ddwu.com.mobile.project.todaysoutfit.data.entity.DiaryEntryEntity
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddDiaryEntryActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var satisfactionDropdown: Spinner
    private lateinit var saveButton: Button
    private lateinit var topOutfitInput: EditText
    private lateinit var bottomOutfitInput: EditText
    private lateinit var outerOutfitInput: EditText
    private lateinit var accessoriesInput: EditText
    private lateinit var memoInput: EditText
    private var selectedLocation: LatLng? = null
    private lateinit var dateEditText: EditText

    private lateinit var searchInput: EditText
    private lateinit var searchButton: Button
    private lateinit var placesClient: PlacesClient
    private lateinit var searchedPlace: TextView
    private lateinit var geocoder: Geocoder

    private lateinit var diaryDatabase: DiaryDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_diary_entry)

        // Initialize UI components
        satisfactionDropdown = findViewById(R.id.satisfaction)
        dateEditText = findViewById(R.id.selectDate)
        topOutfitInput = findViewById(R.id.topOutfit)
        bottomOutfitInput = findViewById(R.id.bottomOutfit)
        outerOutfitInput = findViewById(R.id.outerOutfit)
        accessoriesInput = findViewById(R.id.accessories)
        memoInput = findViewById(R.id.memo)
        saveButton = findViewById(R.id.saveButton)
        searchedPlace = findViewById(R.id.searchedPlace)

        // Intent로 전달받은 날짜 설정
        val receivedDate = intent.getStringExtra("selectedDate")
        if (!receivedDate.isNullOrEmpty()) {
            dateEditText.setText(receivedDate) // 선택된 날짜 설정
        }

        // Set up satisfaction dropdown menu
        val satisfactionOptions = listOf("Good", "Not Bad", "Cold", "Hot")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, satisfactionOptions)
        satisfactionDropdown.setAdapter(adapter)

        // Set up Google Map
        val mapFragment = supportFragmentManager.findFragmentById(R.id.googleMap) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Save button logic
        saveButton.setOnClickListener {
            val date = dateEditText.text.toString()
            val place = searchedPlace.text.toString()
            val satisfaction = satisfactionDropdown.selectedItem.toString()
            val topOutfit = topOutfitInput.text.toString()
            val bottomOutfit = bottomOutfitInput.text.toString()
            val outerOutfit = outerOutfitInput.text.toString()
            val accessories = accessoriesInput.text.toString()
            val memo = memoInput.text.toString()

            val diaryEntry = DiaryEntryEntity(
                date = date,
                location = place,
                top = topOutfit,
                bottom = bottomOutfit,
                outer = outerOutfit,
                accessory = accessories,
                satisfaction = satisfaction,
                memo = memo
            )

            lifecycleScope.launch {
                diaryDatabase.diaryDAO().insertDiary(diaryEntry)
                Toast.makeText(this@AddDiaryEntryActivity, "저장되었습니다!", Toast.LENGTH_SHORT).show()
                finish() // 저장 후 액티비티 종료
            }

            // Save logic here (e.g., store in a database or send to another activity)
            if (selectedLocation != null) {
                // Save location and other data
                val location = selectedLocation!!
                // Example: Save to database
            }
        }

        // 날짜 입력 EditText
        dateEditText = findViewById(R.id.selectDate)

        // EditText 클릭 시 달력 팝업 표시
        dateEditText.setOnClickListener {
            showDatePickerDialog()
        }

        // 추가: Google Places API 초기화
        Places.initialize(applicationContext, getString(R.string.google_maps_key))
        placesClient = Places.createClient(this)

        // 추가: 장소 검색 입력 및 버튼 초기화
        searchInput = findViewById(R.id.searchInput)
        searchButton = findViewById(R.id.searchButton)

        // 추가: 검색 버튼 클릭 리스너
        searchButton.setOnClickListener {
            val query = searchInput.text.toString()
            if (query.isNotEmpty()) {
                searchPlace(query)
            } else {
                Toast.makeText(this, "검색어를 입력하세요", Toast.LENGTH_SHORT).show()
            }
        }

        // Geocoder 초기화
        geocoder = Geocoder(this, Locale.getDefault())

        // Google Map 준비
        mapFragment.getMapAsync { googleMap ->
            this.googleMap = googleMap

            // 지도 클릭 이벤트 리스너 추가
            googleMap.setOnMapClickListener { latLng ->
                updateMarkerAndAddress(latLng)
            }
        }

        // Room DB 인스턴스 초기화
        diaryDatabase = Room.databaseBuilder(
            applicationContext,
            DiaryDatabase::class.java,
            "diary_database"
        ).build()
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        val initialLocation = LatLng(37.5665, 126.9780) // 서울 좌표
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, 10f))

        // 줌 제스처 활성화
        googleMap.uiSettings.isZoomGesturesEnabled = true

        // 스크롤 제스처 활성화 (필요 시)
        googleMap.uiSettings.isScrollGesturesEnabled = true

        // 기타 설정
        googleMap.uiSettings.isMyLocationButtonEnabled = true

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

    // 추가: 장소 검색 함수
    private fun searchPlace(query: String) {
        val token = AutocompleteSessionToken.newInstance()

        val request = FindAutocompletePredictionsRequest.builder()
            .setSessionToken(token)
            .setQuery(query)
            .build()

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response ->
                if (response.autocompletePredictions.isNotEmpty()) {
                    val prediction = response.autocompletePredictions[0]
                    val placeId = prediction.placeId

                    // 장소 세부정보 요청
                    val placeFields = listOf(Place.Field.ADDRESS, Place.Field.LAT_LNG)
                    val placeRequest = FetchPlaceRequest.builder(placeId, placeFields).build()

                    placesClient.fetchPlace(placeRequest)
                        .addOnSuccessListener { placeResponse ->
                            val place = placeResponse.place
                            val address = place.address // 주소
                            val latLng = place.latLng   // 위도와 경도

                            if (address != null && latLng != null) {
                                searchedPlace.text = address // 주소 표시

                                // 마커 이동 및 지도 업데이트
                                val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15f)
                                googleMap.moveCamera(cameraUpdate)
                                googleMap.addMarker(MarkerOptions().position(latLng).title(address))
                            } else {
                                searchedPlace.text = "주소 정보를 찾을 수 없습니다."
                            }
                        }
                        .addOnFailureListener {
                            searchedPlace.text = "장소 세부정보 가져오기 실패"
                        }
                } else {
                    searchedPlace.text = "검색 결과가 없습니다."
                }
            }
            .addOnFailureListener {
                searchedPlace.text = "장소 검색 중 오류 발생"
            }
    }



    // 추가: 장소 세부정보 가져오기
    private fun fetchPlaceDetails(placeId: String) {
        val placeFields = listOf(com.google.android.libraries.places.api.model.Place.Field.LAT_LNG)
        val request = com.google.android.libraries.places.api.net.FetchPlaceRequest.builder(placeId, placeFields).build()

        placesClient.fetchPlace(request).addOnSuccessListener { response ->
            val place = response.place
            val latLng = place.latLng
            if (latLng != null) {
                googleMap.clear()
                googleMap.addMarker(MarkerOptions().position(latLng).title("검색된 장소"))
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                selectedLocation = latLng
            }
        }.addOnFailureListener {
            Toast.makeText(this, "장소 세부정보를 가져오는 중 오류가 발생했습니다", Toast.LENGTH_SHORT).show()
        }
    }

    // 마커 업데이트 및 주소 표시
    private fun updateMarkerAndAddress(latLng: LatLng) {
        // 기존 마커 삭제
        googleMap.clear()

        // 새로운 마커 추가
        googleMap.addMarker(
            MarkerOptions()
                .position(latLng)
                .title("선택한 위치")
        )
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))

        // 주소 가져오기
        try {
            val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (addresses != null && addresses.isNotEmpty()) {
                val address = addresses[0].getAddressLine(0)
                searchedPlace.text = address // 주소 업데이트
            } else {
                searchedPlace.text = "주소를 가져올 수 없습니다."
            }
        } catch (e: Exception) {
            searchedPlace.text = "주소 변환 중 오류 발생: ${e.localizedMessage}"
        }
    }
}