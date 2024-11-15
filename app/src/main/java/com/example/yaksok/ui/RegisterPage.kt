package com.example.yaksok.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterPage(
    goToLoginPage: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, Color.LightGray, RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                // 제목 텍스트
                Text(
                    text = "Welcome!",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(16.dp))

                // 안내 텍스트
                Text(
                    text = "회원가입을 위한 정보를 입력해주세요.",
                    fontSize = 16.sp,
                    color = Color.Gray,
                )
                Spacer(modifier = Modifier.height(24.dp))

                // 아이디 입력 필드
                TextField(
                    value = "",
                    onValueChange = {},
                    placeholder = {
                        Text(
                            text = "아이디",
                            color = Color.LightGray
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors( // 플레이스홀더 텍스트 색상
                        containerColor = Color(240, 240, 240),
                        focusedIndicatorColor = Color.Transparent, // 포커스된 상태의 인디케이터 색상 제거
                        unfocusedIndicatorColor = Color.Transparent // 포커스되지 않은 상태의 인디케이터 색상 제거
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                // 비밀번호 입력 필드
                TextField(
                    value = "",
                    onValueChange = {},
                    placeholder = {
                        Text(
                            text = "비밀번호",
                            color = Color.LightGray
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors( // 플레이스홀더 텍스트 색상
                        containerColor = Color(240, 240, 240),
                        focusedIndicatorColor = Color.Transparent, // 포커스된 상태의 인디케이터 색상 제거
                        unfocusedIndicatorColor = Color.Transparent // 포커스되지 않은 상태의 인디케이터 색상 제거
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))
                // 선 긋기
                Divider(
                    color = Color.LightGray,      // 선 색상
                    thickness = 1.dp,        // 선 두께
                )
                Spacer(modifier = Modifier.height(12.dp))
                // 이름 입력 필드
                TextField(
                    value = "",
                    onValueChange = {},
                    placeholder = {
                        Text(
                            text = "이름",
                            color = Color.LightGray
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors( // 플레이스홀더 텍스트 색상
                        containerColor = Color(240, 240, 240),
                        focusedIndicatorColor = Color.Transparent, // 포커스된 상태의 인디케이터 색상 제거
                        unfocusedIndicatorColor = Color.Transparent // 포커스되지 않은 상태의 인디케이터 색상 제거
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                // 전화번호 입력 필드
                TextField(
                    value = "",
                    onValueChange = {},
                    placeholder = {
                        Text(
                            text = "전화번호",
                            color = Color.LightGray
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors( // 플레이스홀더 텍스트 색상
                        containerColor = Color(240, 240, 240),
                        focusedIndicatorColor = Color.Transparent, // 포커스된 상태의 인디케이터 색상 제거
                        unfocusedIndicatorColor = Color.Transparent // 포커스되지 않은 상태의 인디케이터 색상 제거
                    )
                )
                Spacer(modifier = Modifier.height(30.dp))
                // 회원가입 버튼
                Button(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(122, 178, 211))
                ) {
                    Text("회원가입")
                }
            }
        }
    }
}