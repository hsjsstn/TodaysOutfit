package ddwu.com.mobile.project.todaysoutfit.data

class WeatherDTO {
    // Getter & Setter
    var temperature: Double = 0.0 // 현재 기온
    var maxTemperature: Double = 0.0 // 최고 기온
    var minTemperature: Double = 0.0 // 최저 기온
    var condition: String? = null // 날씨 상태 (맑음, 흐림, 비 등)
}