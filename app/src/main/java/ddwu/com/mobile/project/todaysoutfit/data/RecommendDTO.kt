package ddwu.com.mobile.project.todaysoutfit.data

data class RecommendDTO(
    val temperatureRange: String,    // 기온 범위 (예: "30+")
    val clothingSuggestion: String  // 추천 옷차림 (예: "반팔, 반바지")
)