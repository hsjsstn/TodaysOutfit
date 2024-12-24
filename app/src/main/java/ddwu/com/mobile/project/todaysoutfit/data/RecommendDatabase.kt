package ddwu.com.mobile.project.todaysoutfit.data

object RecommendDatabase {

    private val recommendations = listOf(
        "30+" to "반팔, 반바지",
        "20-25" to "반팔, 얇은 외투",
        "10-20" to "긴팔, 가벼운 코트",
        "0-10" to "패딩, 스웨터",
        "-10-" to "두꺼운 패딩, 방한용품"
    )

    fun getRecommendation(temperature: Double): RecommendDTO? {
        val range = when {
            temperature >= 30 -> "30+"
            temperature in 20.0..25.0 -> "20-25"
            temperature in 10.0..20.0 -> "10-20"
            temperature in 0.0..10.0 -> "0-10"
            else -> "-10-"
        }

        val suggestion = recommendations.find { it.first == range }?.second
        return if (suggestion != null) RecommendDTO(range, suggestion) else null
    }
}