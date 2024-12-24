package ddwu.com.mobile.project.todaysoutfit.data

class DiaryEntryDTO {
    // Getter & Setter
    var date: String? = null // 기록 날짜
    var location: String? = null // 방문 장소
    var maxTemperature: Double = 0.0 // 최고 기온
    var minTemperature: Double = 0.0 // 최저 기온
    var top: String? = null // 상의
    var bottom: String? = null // 하의
    var outer: String? = null // 아우터
    var accessory: String? = null // 패션 소품
    var satisfaction: String? = null // 만족도 (Good, Not Bad, Cold, Hot)
    var memo: String? = null // 메모
}