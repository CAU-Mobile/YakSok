package com.example.yaksok.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun ManageYaksokPage(
    goToYaksokDetailPage: (Any?) -> Unit,
    viewModel: YaksokViewModel
) {
    LaunchedEffect(Unit) {
        viewModel.loadYaksokList()
    }

    val yaksokList by viewModel.YaksokList.collectAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "나의 약속",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color(58, 58, 58)
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .clip(RoundedCornerShape(16.dp))
                .padding(16.dp)
                .background(Color(230, 230, 230))
        ) {
            items(yaksokList) { appointment ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp)) // Column 모서리를 둥글게 설정
                        .background(Color(185, 229, 232)) // 배경색
                        .border(2.dp, Color.Gray, RoundedCornerShape(20.dp)) // 둥근 테두리
                        .padding(24.dp) // 내부 여백
                        .clickable { goToYaksokDetailPage(appointment.documentId) } // 클릭 가능하도록 설정, 'ripple' 효과 자동적용
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = appointment.name,  // 약속 이름
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Light,
                            color = Color(58, 58, 58)
                        )

                        Spacer(modifier = Modifier.width(40.dp))
                        Text(
                            text = DateTimeFormatter
                                .ofPattern("yyyy년 MM월 dd일\r\nHH시 mm분")
                                .withZone(ZoneId.of("Asia/Seoul"))
                                .format(appointment.time.toDate().toInstant()),  // 약속 시간
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Light,
                            color = Color(58, 58, 58)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(
                        color = Color.LightGray,      // 선 색상
                        thickness = 1.dp,        // 선 두께
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = "장소 : ${appointment.placeName}",
                            fontSize = 15.sp,
                            color = Color.Black
                        )
                        Text(
                            text = "위치 : ${appointment.placeAddress}",  // 장소
                            fontSize = 15.sp,
                            color = Color.Black
                        )
                        Text(
                            text = "참여자 : ${appointment.memberIds.joinToString(", ")}", // 참여자
                            fontSize = 15.sp,
                            color = Color.Black
                        )

                    }
                }
                Spacer(modifier = Modifier.height(16.dp)) // 각 항목 사이에 여백 추가
            }
        }
    }
}
