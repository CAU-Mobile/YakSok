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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ManageYaksokPage(
    goToYaksokDetailPage: () -> Unit
) {
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
            color = Color(58,58,58)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .clip(RoundedCornerShape(16.dp))
                .padding(16.dp)
                .background(Color(230, 230, 230))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())

            ) {
                repeat(5) { // 반복적으로 약속 Column 생성
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp)) // Column 모서리를 둥글게 설정
                            .background(Color(185, 229, 232)) // 배경색
                            .border(2.dp, Color.Gray, RoundedCornerShape(20.dp)) // 둥근 테두리
                            .padding(24.dp) // 내부 여백
                            .clickable { goToYaksokDetailPage() } // 클릭 가능하도록 설정, 'ripple' 효과 자동적용
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "학생회 모임",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Light,
                                color = Color(58, 58, 58)
                            )
                            Spacer(modifier = Modifier.width(40.dp))
                            Text(
                                text = "2024-12-25",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Light,
                                color = Color.Gray
                            )
                            Text(
                                text = "13:00",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Light,
                                color = Color.Gray
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Divider(
                            color = Color.LightGray,      // 선 색상
                            thickness = 1.dp,        // 선 두께
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "장소 : 중앙대학교 310관 728호",
                                fontSize = 15.sp,
                                color = Color.Black
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "참여자 : 박수빈, 박예빈, 임결, 최지원",
                                fontSize = 15.sp,
                                color = Color.Black
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Preview
@Composable
fun ManageYaksokPagePreview() {
    ManageYaksokPage(goToYaksokDetailPage = {})
}