package ddwu.com.mobile.project.todaysoutfit.data.network

data class WeatherResponse(
    val response: Response
)

data class Response(
    val body: Body
)

data class Body(
    val items: Items
)

data class Items(
    val item: List<WeatherItem>
)

data class WeatherItem(
    val category: String, // 데이터 종류 (예: T1H: 기온, RN1: 강수량)
    val obsrValue: String, // 관측값
    val fcstValue: String? // 예측값
)