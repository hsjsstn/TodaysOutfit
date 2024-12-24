package ddwu.com.mobile.project.todaysoutfit.data

class RecommendDAO {
    // 기온 범위와 추천 옷차림 데이터 초기화
    private val recommendations: MutableMap<String, String> = HashMap()

    init {
        recommendations["30+"] = "반팔, 반바지"
        recommendations["20-25"] = "반팔, 얇은 외투"
        recommendations["10-20"] = "긴팔, 가벼운 코트"
        recommendations["0-10"] = "패딩, 스웨터"
        recommendations["-10-"] = "두꺼운 패딩, 방한용품"
    }

    fun getRecommendation(temperatureRange: String): String {
        return recommendations.getOrDefault(temperatureRange, "추천 데이터 없음")
    }
}