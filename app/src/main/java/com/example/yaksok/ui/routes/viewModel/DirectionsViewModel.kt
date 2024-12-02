package com.example.yaksok.ui.routes.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.yaksok.feature.route.domain.model.DirectionsEntity
import com.example.yaksok.feature.route.domain.model.DirectionsTransitDetailsEntity
import com.example.yaksok.feature.route.domain.model.DirectionsTransitLineEntity
import com.example.yaksok.feature.route.domain.model.DirectionsTransitStopEntity
import com.example.yaksok.feature.route.domain.model.DirectionsTransitVehicleEntity
import com.example.yaksok.feature.route.domain.model.LatLngEntity
import com.example.yaksok.feature.route.domain.model.TimeZoneTextValueObjectEntity
import com.example.yaksok.feature.route.domain.usecase.GetDirWithArrTmRpUseCase
import com.example.yaksok.feature.route.domain.usecase.GetDirWithDepTmRpUseCase
import com.example.yaksok.feature.route.domain.usecase.GetDirWithTmRpUseCase
import com.example.yaksok.feature.route.domain.usecase.GetDirectionsUseCase
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

class DirectionsViewModel(
    private val getDirectionsUseCase: GetDirectionsUseCase,
    private val getDirWithDepTmRpUseCase: GetDirWithDepTmRpUseCase,
    private val getDirWithTmRpUseCase: GetDirWithTmRpUseCase,
    private val getDirWithArrTmRpUseCase: GetDirWithArrTmRpUseCase
) : ViewModel() {

    private val _directionsResult = MutableStateFlow<DirectionsEntity?>(null)
    val directionsResult: StateFlow<DirectionsEntity?> = _directionsResult

    private val _selectedRouteIndex = MutableStateFlow<Int?>(0)
    val selectedRouteIndex: StateFlow<Int?> = _selectedRouteIndex

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    //string 형태로 바꾸거나 문자로 검색한 출발지(사용자의 위치)
    private val _origin = MutableLiveData<String>()
    val origin: LiveData<String> get() = _origin

    //string 형태로 바꾸거나 문자로 검색한 도착지(목적지)
    private val _destination = MutableLiveData<String>()
    val destination: LiveData<String> get() = _destination

    //transit, driving, walking 등
    private val _mode = MutableStateFlow<String>("transit")
    val mode: StateFlow<String> = _mode

    //더 선호하는 대중교통 수단
    private val _transitMode = MutableLiveData<String>()
    val transitMode: LiveData<String> get() = _transitMode

    //더 선호하는 방식 (less_walking 등)
    private val _routingPreference = MutableLiveData<String>()
    val routingPreference: LiveData<String> get() = _routingPreference

    //출/도착 시간 선택한 경우
    private val _selectedTime = MutableStateFlow<LocalTime?>(null)
    val selectedTime: StateFlow<LocalTime?> = _selectedTime

    //    //경로 선택하기 전 보여줄 간단한 소개들
    private val _routeSelectionText = MutableStateFlow<List<String>>(emptyList())
    val routeSelectionText: StateFlow<List<String>> get() = _routeSelectionText

    //시간 선택 창 옆 버튼...
    private val _isDepArrNone = MutableStateFlow(0)
    val isDepArrNone: StateFlow<Int> = _isDepArrNone

    private val _polylines = MutableStateFlow<List<PolylineOptions>>(emptyList())
    val polylines: StateFlow<List<PolylineOptions>> = _polylines

    private var _destLocationLatLng = MutableLiveData<LatLng>()
    val destLocationLatLng: LiveData<LatLng> get() = _destLocationLatLng

    private val _directionExplanations = MutableLiveData<String>()
    val directionExplanations: LiveData<String> get() = _directionExplanations

    private var _startLocation = MutableLiveData<LatLng>()
    val startLocation: LiveData<LatLng> get() = _startLocation

    private var _passedOrigin = MutableStateFlow<String>("")
    val passedOrigin: StateFlow<String> get() = _passedOrigin

    private var _passedDestination = MutableStateFlow<String>("")
    val passedDestination: StateFlow<String> get() = _passedDestination

    private val _passedTime = MutableStateFlow<LocalTime?>(null)
    val passedTime: StateFlow<LocalTime?> get() = _passedTime

    private val _changedTime = MutableStateFlow<Long?>(0)
    val changedTime: StateFlow<Long?> get() = _changedTime


    //추가한 부분
    private val _selectedPolyline = MutableStateFlow<List<LatLng>>(emptyList())
    val selectedPolyline: StateFlow<List<LatLng>> = _selectedPolyline

    private val _mapBounds = MutableStateFlow<LatLngBounds?>(null)
    val mapBounds: StateFlow<LatLngBounds?> = _mapBounds
    //

    private val _showRouteDialog = MutableStateFlow(false)
    val showRouteDialog: StateFlow<Boolean> get() = _showRouteDialog

    fun setEverything(
        origin: String,
        destination: String,
        passedMode: String,
        timeSelectedState: Int,
        selectedHourMinute: Pair<Int, Int>,
        passedTM: String = "",
        passedRP: String = ""
        //TODO TM, RP 매개변수로 받아오기
    ) {
        _passedOrigin.value = origin
        _passedDestination.value = destination
        _mode.value = passedMode
        _isDepArrNone.value = timeSelectedState
        setTime(selectedHourMinute.first, selectedHourMinute.second)
        _transitMode.value = passedTM
        _routingPreference.value = passedRP

        // mode에 따라 1차 가르기 - transit 제외하고는 시간 설정 못하게
        when (mode.value) {
            "transit" -> {
                getDirByTransit()
            }
            else -> fetchDirections()
        }
    }

    fun getDirByTransit() {
        when (isDepArrNone.value) {
            0 -> {
                getDirWithTmRp()
            }

            1 -> {
                getDirWithDep()
            }

            else -> {
                getDirWithArr()
            }
        }
    }

    fun setTime(hour: Int, minute: Int) {
        _selectedTime.value = LocalTime.of(hour, minute)
        selectedTime.value?.let { getUnixTimestamp(it) }
    }

    private fun getUnixTimestamp(selectedTime: LocalTime) {
        val currentDate = LocalDate.now()
        var dateTime = LocalDateTime.of(currentDate, selectedTime)

        if (dateTime.toLocalTime().isBefore(LocalTime.now())) {
            dateTime = dateTime.plusDays(1)
        }

        val zonedDateTime = ZonedDateTime.of(dateTime, ZoneId.of("Asia/Seoul"))
        _changedTime.value = zonedDateTime.toEpochSecond()
        Log.d("확인 changedTime", changedTime.value.toString())
    }

    fun setLocations(
        start: LatLng,
        destination: LatLng,
        startAddress: String,
        endAddress: String
    ) {
        _startLocation.value = start
        _destLocationLatLng.value = destination
        _origin.value = startAddress
        _destination.value = endAddress
    }

    fun setSelectedRouteIndex(indexNum: Int) {
        _selectedRouteIndex.value = indexNum ?: 0
        Log.d("123123", "${indexNum}")
    }

    //대중교통, 시간 없이
    fun getDirWithTmRp() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = getDirWithTmRpUseCase(
                    passedOrigin.value,
                    passedDestination.value,
                    transitMode = transitMode.value ?: "",
                    transitRoutingPreference = routingPreference.value ?: ""
                )
                withContext(Dispatchers.Main) {
                    _directionsResult.value = result
                    setRouteSelectionText()
                    Log.d("확인 routeSelectionText", routeSelectionText.value.toString())
                    val tempLatLngs = _directionsResult.value?.routes?.get(0)?.legs?.get(0)
                    if (tempLatLngs != null) {
                        setLocations(
                            LatLng(
                                tempLatLngs.totalStartLocation.lat,
                                tempLatLngs.totalStartLocation.lng
                            ),
                            LatLng(
                                tempLatLngs.totalEndLocation.lat,
                                tempLatLngs.totalEndLocation.lng
                            ),
                            tempLatLngs.totalStartAddress,
                            tempLatLngs.totalEndAddress
                        )
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _error.postValue(e.message)
                }
            }
        }
    }

    //대중교통, 출발시간
    fun getDirWithDep() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = getDirWithDepTmRpUseCase(
                    passedOrigin.value,
                    passedDestination.value,
                    departureTime = changedTime.value.toString().toInt(),
                    transitMode = transitMode.value ?: "",
                    transitRoutingPreference = routingPreference.value ?: ""
                )
                withContext(Dispatchers.Main) {
                    _directionsResult.value = result
                    setRouteSelectionText()
                    Log.d("확인 routeSelectionText", routeSelectionText.value.toString())
                    val tempLatLngs = _directionsResult.value?.routes?.get(0)?.legs?.get(0)
                    if (tempLatLngs != null) {
                        setLocations(
                            LatLng(
                                tempLatLngs.totalStartLocation.lat,
                                tempLatLngs.totalStartLocation.lng
                            ),
                            LatLng(
                                tempLatLngs.totalEndLocation.lat,
                                tempLatLngs.totalEndLocation.lng
                            ),
                            tempLatLngs.totalStartAddress,
                            tempLatLngs.totalEndAddress
                        )
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _error.postValue(e.message)
                }
            }
        }
    }

    //도착시간, 대중교통
    fun getDirWithArr() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = getDirWithArrTmRpUseCase(
                    passedOrigin.value,
                    passedDestination.value,
                    arrivalTime = changedTime.value.toString().toInt(),
                    transitMode = transitMode.value ?: "",
                    transitRoutingPreference = routingPreference.value ?: ""
                )
                withContext(Dispatchers.Main) {
                    _directionsResult.value = result
                    setRouteSelectionText()
                    Log.d("확인 routeSelectionText", routeSelectionText.value.toString())
                    val tempLatLngs = _directionsResult.value?.routes?.get(0)?.legs?.get(0)
                    if (tempLatLngs != null) {
                        setLocations(
                            LatLng(
                                tempLatLngs.totalStartLocation.lat,
                                tempLatLngs.totalStartLocation.lng
                            ),
                            LatLng(
                                tempLatLngs.totalEndLocation.lat,
                                tempLatLngs.totalEndLocation.lng
                            ),
                            tempLatLngs.totalStartAddress,
                            tempLatLngs.totalEndAddress
                        )
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _error.postValue(e.message)
                }
            }
        }
    }

    private fun fetchDirections() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = getDirectionsUseCase(
                    passedOrigin.value,
                    passedDestination.value,
                    mode.value
                )
                withContext(Dispatchers.Main) {
                    _directionsResult.value = result
                    setRouteSelectionText()
                    Log.d("확인 routeSelectionText", routeSelectionText.value.toString())
                    val tempLatLngs = _directionsResult.value?.routes?.get(0)?.legs?.get(0)
                    if (tempLatLngs != null) {
                        setLocations(
                            LatLng(
                                tempLatLngs.totalStartLocation.lat,
                                tempLatLngs.totalStartLocation.lng
                            ),
                            LatLng(
                                tempLatLngs.totalEndLocation.lat,
                                tempLatLngs.totalEndLocation.lng
                            ),
                            tempLatLngs.totalStartAddress,
                            tempLatLngs.totalEndAddress
                        )
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _error.postValue(e.message)
                }
            }
        }
    }

    fun closeRouteDialog() {
        _showRouteDialog.value = false
    }

    fun afterSelecting() {
        viewModelScope.launch(Dispatchers.Default) {
            updatePolyLineWithColors()
            updateMapBounds()
            setDirectionsResult()
        }
    }

    fun setDirectionsResult() {
        if (_directionsResult.value != null) {
            formatDirectionsExplanations(_directionsResult.value!!)
        } else {
            _error.postValue("_direction null")
            Log.d("확인 setDirections", "null")
        }
    }

    private fun setRouteSelectionText() {
        viewModelScope.launch(Dispatchers.Default) {
            if (_directionsResult.value != null) {
                Log.d("확인 setDirections", "${_directionsResult.value}")
                formatRouteSelectionText(_directionsResult.value!!)
            } else {
                _error.postValue("출발지와 목적지를 다시 확인해 주세요.")
                Log.d("확인 setDirections", "null")
                _routeSelectionText.value = emptyList()
                //emptyOrNull
            }
        }
    }


    private fun formatRouteSelectionText(directions: DirectionsEntity) {
        val resultsList = mutableListOf<String>()
        refreshIndex()

        directions.routes.size
        var routeIndex = 1
        directions.routes.forEach { route ->
            val resultText = StringBuilder()
            val resultText1 = StringBuilder()

            resultText.append("🔵경로 ${routeIndex}\n")
            route.legs.forEach { leg ->
                resultText1.append("  예상 소요 시간 : ${leg.totalDuration.text}")
                if (mode.value == "transit") {
                    resultText.append("\n🕐${leg.totalArrivalTime.text}에 도착 예정입니다.\n")
                } else {
                    resultText.append("\n")
                }
                resultText1.append("\n")

                val resultText2 = StringBuilder()

                var num = 1
                leg.steps.forEach { step ->
                    resultText2.append("✦${num}:")
                    if (step.travelMode == "TRANSIT") {
                        if (step.transitDetails.line.shortName != "") {
                            resultText2.append(" [${step.transitDetails.line.shortName}]")
                        } else if (step.transitDetails.line.name != "") {
                            resultText2.append(" [${step.transitDetails.line.name}]")
                        } else {
                            //
                        }
                    }
                    Log.d("확인 travelMode", "${step.travelMode.toString()}")

                    resultText2.append(" ${step.htmlInstructions} (${step.stepDuration.text})\n")
                    num++
                }
                resultText1.append(resultText2)
            }
            resultText.append(resultText1)
            resultsList.add(resultText.toString())
            routeIndex++
        }
        Log.d("확인 리스트 인덱스", "${resultsList.size}")
        _showRouteDialog.value = true
        _routeSelectionText.value = resultsList
        Log.d("확인 setDirections", "stringbuilder ${resultsList}")
    }

    fun refreshIndex() {
        _selectedRouteIndex.value = 0
    }

    fun selectRoute(index: Int) {
        _selectedRouteIndex.value = index
        updatePolyLineWithColors()
        updateSelectedPolyline()
    }

    //추가한 부분임
    private fun updateSelectedPolyline() {
        viewModelScope.launch {
            val selectedRoute =
                _directionsResult.value?.routes?.getOrNull(_selectedRouteIndex.value ?: 0)
            val polyline = selectedRoute?.overviewPolyline?.points
            if (polyline != null) {
                _selectedPolyline.value = PolyUtil.decode(polyline)
            }
        }
    }
    //

    private fun updatePolyLineWithColors() {
        try {
            val routes = _directionsResult.value?.routes
            val polylines = mutableListOf<PolylineOptions>()

            routes?.get(_selectedRouteIndex.value ?: 0)?.legs?.forEach { leg ->
                leg.steps.forEach { step ->
                    val decodedPoints = PolyUtil.decode(step.polyline.points ?: "")
                    val color = hexToColorInt(step.transitDetails?.line?.color ?: "#FF0000")

                    val coloredLine = PolylineOptions()
                        .addAll(decodedPoints)
                        .width(10f)
                        .color(color)

                    polylines.add(coloredLine)
                }
            }
            _polylines.value = polylines
        } catch (e: Exception) {
            _error.postValue(e.message)
        }
    }

    private fun updateMapBounds() {
        viewModelScope.launch(Dispatchers.Default) {
            try {
                val routes = _directionsResult.value?.routes
                val selectedRoute = routes?.get(_selectedRouteIndex.value ?: 0)
                val boundsBuilder = LatLngBounds.Builder()
                var hasPoints = false

                selectedRoute?.legs?.forEach { leg ->
                    leg.steps.forEach { step ->
                        val decodedPoints = PolyUtil.decode(step.polyline.points ?: "")
                        if (decodedPoints.isNotEmpty()) {
                            decodedPoints.forEach { point ->
                                boundsBuilder.include(point)
                            }
                            hasPoints = true
                        }
                    }
                }

                if (hasPoints) {
                    val bounds = boundsBuilder.build()
                    withContext(Dispatchers.Main) {
                        _mapBounds.value = bounds
                    }
                } else {
                    Log.e("updateMapBounds", "bound안에 포인트 없음")
                }
            } catch (e: Exception) {
                Log.e("updateMapBounds", "map bound 업데이트 중 문제 발생", e)
            }
        }
    }

    private fun hexToColorInt(hexColor: String): Int {
        return try {
            android.graphics.Color.parseColor("#${hexColor.removePrefix("#")}")
        } catch (e: IllegalArgumentException) {
            android.graphics.Color.GRAY
        }
    }

    private fun formatDirectionsExplanations(directions: DirectionsEntity) {
        val resultText = StringBuilder()
        val finalText = StringBuilder()
        Log.d("확인 index 상태", "${selectedRouteIndex.value}")

        directions.routes.get(_selectedRouteIndex.value!!).legs.forEach { leg ->
            resultText.append("🗺️목적지까지 ${leg.totalDistance.text},\n")
            resultText.append("앞으로 ${leg.totalDuration.text} 뒤")
            if (mode.value == "transit") {
                resultText.append("인\n🕐${leg.totalArrivalTime.text}에 도착 예정입니다.\n")
            } else {
                resultText.append(" 도착 예정입니다.\n")
            }
            resultText.append("\n")
            var num = 1
            val resultText1 = StringBuilder()
            leg.steps.forEach { step ->
                resultText1.append("🔷${num}\n")
                resultText1.append("*  상세설명:")

                if (step.travelMode == "TRANSIT") {
                    if (step.transitDetails.line.shortName != "") {
                        resultText1.append(" [${step.transitDetails.line.shortName}]")
                    } else if (step.transitDetails.line.name != "") {
                        resultText1.append(" [${step.transitDetails.line.name}]")
                    } else {
                        //
                    }
                }
                Log.d("확인 travelMode", "${step.travelMode.toString()}")

                resultText1.append(" ${step.htmlInstructions}\n")
                resultText1.append("*  소요시간: ${step.stepDuration.text}\n")
                resultText1.append("*  구간거리: ${step.distance.text}\n")

                if (step.transitDetails != DirectionsTransitDetailsEntity(
                        DirectionsTransitStopEntity(LatLngEntity(0.0, 0.0), ""),
                        TimeZoneTextValueObjectEntity("", "", 0.0),
                        DirectionsTransitStopEntity(LatLngEntity(0.0, 0.0), ""),
                        TimeZoneTextValueObjectEntity("", "", 0.0),
                        (""),
                        0,
                        DirectionsTransitLineEntity(
                            emptyList(),
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            DirectionsTransitVehicleEntity("", "", "", "")
                        ),
                        0,
                        ""
                    )
                ) {
                    resultText1.append("|    탑승 장소: ${step.transitDetails.departureStop.name}\n")
                    resultText1.append("|    하차 장소: ${step.transitDetails.arrivalStop.name}\n")
                    resultText1.append("|    ${step.transitDetails.numStops}")
                    resultText1.append(categorizeTransportation(step.transitDetails.line.vehicle.type))
                    resultText1.append("\n\n")
                } else {
                    resultText1.append("\n\n\n")
                }

                num++
            }
            resultText.append(resultText1)
        }
        _directionExplanations.postValue(resultText.toString())
    }

    // 교통 수단을 카테고라이즈하는 메서드
    private fun categorizeTransportation(transportationType: String): String {
        return when (transportationType) {
            "BUS" -> {
                "개 정류장 이동🚍\n"
            }

            "CABLE_CAR" -> {
                " 케이블 카 이용🚟\n"
            }

            "COMMUTER_TRAIN" -> {
                "개 역 이동🚞\n"
            }

            "FERRY" -> {
                " 페리 이용⛴️\n"
            }

            "FUNICULAR" -> {
                " 푸니큘러 이용🚋\n"
            }

            "GONDOLA_LIFT" -> {
                " 곤돌라 리프트 이용🚠\n"
            }

            "HEAVY_RAIL" -> {
                "개 역 이동🛤️\n"
            }

            "HIGH_SPEED_TRAIN" -> {
                "개 역 이동🚄\n"
            }

            "INTERCITY_BUS" -> {
                "개 정류장 이동🚌\n"
            }

            "LONG_DISTANCE_TRAIN" -> {
                "개 역 이동🚂\n"
            }

            "METRO_RAIL" -> {
                "개 역 이동🚇\n"
            }

            "MONORAIL" -> {
                "개 역 이동🚝\n"
            }

            "OTHER" -> {
                " 이동\n"
            }

            "RAIL" -> {
                "개 역 이동🚃\n"
            }

            "SHARE_TAXI" -> {
                " 공유 택시 이용🚖\n"
            }

            "SUBWAY" -> {
                "개 역 이동🚉\n"
            }

            "TRAM" -> {
                "개 역 트램으로 이동🚊\n"
            }

            "TROLLEYBUS" -> {
                "개 정류장 트롤리버스로 이동🚎\n"
            }

            else -> {
                " 이동\n"
            }
        }
    }
}

class DirectionsViewModelFactory(
    private val getDirectionsUseCase: GetDirectionsUseCase,
    private val getDirWithDepTmRpUseCase: GetDirWithDepTmRpUseCase,
    private val getDirWithTmRpUseCase: GetDirWithTmRpUseCase,
    private val getDirWithArrTmRpUseCase: GetDirWithArrTmRpUseCase
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DirectionsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DirectionsViewModel(
                getDirectionsUseCase,
                getDirWithDepTmRpUseCase,
                getDirWithTmRpUseCase,
                getDirWithArrTmRpUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}