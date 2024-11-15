package com.example.yaksok.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ManageYaksokPage() {
    Column (
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "나의 약속",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(16.dp)
        ){
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp)) // Column 모서리를 둥글게 설정
                    .background(Color(185, 229, 232)) // 배경색
                    .border(2.dp, Color.Gray, RoundedCornerShape(20.dp)) // 둥근 테두리
                    .padding(16.dp) // 내부 여백
            ) {
                Text(
                    text = "약속 이름",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Light,
                    color = Color.Black
                )
                Text(
                    text = "약속 장소",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Light,
                    color = Color.Gray
                )
                Text(
                    text = "약속 시간",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Light,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp)) // Column 모서리를 둥글게 설정
                    .background(Color(185, 229, 232)) // 배경색
                    .border(2.dp, Color.Gray, RoundedCornerShape(20.dp)) // 둥근 테두리
                    .padding(16.dp) // 내부 여백
            ) {
                Text(
                    text = "약속 이름",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Light,
                    color = Color.Black
                )
                Text(
                    text = "약속 장소",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Light,
                    color = Color.Gray
                )
                Text(
                    text = "약속 시간",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Light,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp)) // Column 모서리를 둥글게 설정
                    .background(Color(185, 229, 232)) // 배경색
                    .border(2.dp, Color.Gray, RoundedCornerShape(20.dp)) // 둥근 테두리
                    .padding(16.dp) // 내부 여백
            ) {
                Text(
                    text = "약속 이름",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Light,
                    color = Color.Black
                )
                Text(
                    text = "약속 장소",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Light,
                    color = Color.Gray
                )
                Text(
                    text = "약속 시간",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Light,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp)) // Column 모서리를 둥글게 설정
                    .background(Color(185, 229, 232)) // 배경색
                    .border(2.dp, Color.Gray, RoundedCornerShape(20.dp)) // 둥근 테두리
                    .padding(16.dp) // 내부 여백
            ) {
                Text(
                    text = "약속 이름",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Light,
                    color = Color.Black
                )
                Text(
                    text = "약속 장소",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Light,
                    color = Color.Gray
                )
                Text(
                    text = "약속 시간",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Light,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp)) // Column 모서리를 둥글게 설정
                    .background(Color(185, 229, 232)) // 배경색
                    .border(2.dp, Color.Gray, RoundedCornerShape(20.dp)) // 둥근 테두리
                    .padding(16.dp) // 내부 여백
            ) {
                Text(
                    text = "약속 이름",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Light,
                    color = Color.Black
                )
                Text(
                    text = "약속 장소",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Light,
                    color = Color.Gray
                )
                Text(
                    text = "약속 시간",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Light,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}