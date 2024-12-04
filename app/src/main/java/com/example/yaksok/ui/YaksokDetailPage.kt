package com.example.yaksok.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.example.yaksok.R
import com.example.yaksok.query.Appointment
import com.example.yaksok.query.UsersQuery
import com.example.yaksok.query.UsersQueryCoroutine
import com.example.yaksok.ui.distance.viewModel.DistanceViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YaksokDetailPage(
    appointment: Appointment,
    viewModel: YaksokViewModel,
    distanceViewModel: DistanceViewModel
) {
    val context = LocalContext.current
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    // 위치 권한 요청하는 부분
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val locationPermissionsGranted = permissions.entries.all { it.value }
        if (locationPermissionsGranted) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    location?.let {
                        Log.d(
                            "yaksok 현재위치",
                            "권한 승인 후 최초 위치: 위도: ${it.latitude}, 경도: ${it.longitude}"
                        )
                        distanceViewModel.appointmentLocation = Location("").apply {
                            latitude = appointment.geoPoint.latitude
                            longitude = appointment.geoPoint.longitude
                        }
                    }
                }
            }
        } else {
            Log.d("yaksok 현재위치", "위치 권한이 거부됨")
        }
    }

    LaunchedEffect(Unit) {
        launcher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    LaunchedEffect(appointment.memberIds) {
        distanceViewModel.appointmentLocation?.let {
            appointment.memberIds.forEach { memberId ->
                viewModel.loadFriendNumber(memberId)
                viewModel.loadFriendName(memberId)
                val user = UsersQueryCoroutine.getUserWithCode(memberId)
                val thisLocation = Location("").apply {
                    user?.let {
                        latitude = user.location.latitude
                        longitude = user.location.longitude
                    }
                }
                distanceViewModel.updateDistance(
                    memberId,
                    thisLocation
                )
            }
        }
    }

    // 위치 업데이트 및 거리 계산하는 부분
    LaunchedEffect(Unit) {
        while (true) {
            distanceViewModel.appointmentLocation?.let {
                appointment.memberIds.forEach { memberId ->
                    viewModel.loadFriendNumber(memberId)
                    viewModel.loadFriendName(memberId)
                    val user = UsersQueryCoroutine.getUserWithCode(memberId)
                    val thisLocation = Location("").apply {
                        user?.let {
                            latitude = user.location.latitude
                            longitude = user.location.longitude
                        }
                    }
                    distanceViewModel.updateDistance(
                        memberId,
                        thisLocation
                    )
                }
            }
            delay(10000) // 1분간격
        }
    }

    val friendNumbers by viewModel.friendNumbers.observeAsState(emptyMap())
    val friendNames by viewModel.friendNames.observeAsState(emptyMap())

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(25.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "약속 상세",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(58, 58, 58)
            )
        }
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(223, 242, 235))
                    .padding(25.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = appointment.name,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(58, 58, 58)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = DateTimeFormatter
                            .ofPattern("MM월 dd일\r\nHH시 mm분")
                            .withZone(ZoneId.of("Asia/Seoul"))
                            .format(appointment.time.toDate().toInstant()),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Light,
                        color = Color(122, 178, 211)
                    )
                }
                Divider(
                    color = Color(122, 178, 211),      // 선 색상
                    thickness = 1.dp,        // 선 두께
                )
                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                    //아래를 살리면 지도가 안보여서 일단 주석처리했습니다
//                        .clip(RoundedCornerShape(5.dp))
                ) {
                    Text(
                        text = appointment.placeName
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    if (appointment.placeLat != null && appointment.placeLng != null) {
                        val plLat = appointment.placeLat
                        val plLng = appointment.placeLng

                        val placePair = LatLng(plLat, plLng)
                        val cameraPositionState = rememberCameraPositionState {
                            position = CameraPosition.fromLatLngZoom(
                                placePair,
                                15f
                            )
                        }
                        GoogleMap(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            cameraPositionState = cameraPositionState,
                        ) {
                            Marker(
                                state = MarkerState(
                                    position = placePair
                                ),
                                title = appointment.placeName
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(BorderStroke(1.dp, Color(122, 178, 211)))
                        .padding(16.dp)
                ) {
                    Text(
                        text = appointment.details,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraLight,
                        color = Color(58, 58, 58)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "약속 참여자",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(58, 58, 58)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        items(appointment.memberIds) { memberId ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = "약속 참여자",
                    modifier = Modifier.size(30.dp)
                )
                val friendName = friendNames[memberId] ?: "이름 가져오는중"
                Column {
                    Text(
                        text = friendName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Light,
                        color = Color(58, 58, 58)
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    val friendNumber = friendNumbers[memberId] ?: "번호 가져오는 중"
                    // 친구 번호 표시
                    Text(
                        text = friendNumber,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Light,
                        color = Color(58, 58, 58)
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val distanceValue = distanceViewModel.getDistanceWithUserId(memberId)
                    Text(
                        text = "남은 거리: ${String.format("%.1f", distanceValue)}km",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    if (distanceValue <= 100) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "도착 직전",
                            fontSize = 12.sp,
                            color = Color(122, 178, 211),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
        }
    }
}
