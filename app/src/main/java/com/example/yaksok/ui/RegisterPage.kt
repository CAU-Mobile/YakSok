package com.example.yaksok.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
                .background(Color(223, 242, 235))
                .padding(innerPadding)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                // 제목 텍스트
                Text(
                    text = "약속어때",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(74, 98, 138) // 색상은 이미지 참고하여 설정
                )
                Spacer(modifier = Modifier.height(16.dp))

                // 안내 텍스트
                Text(
                    text = "회원가입을 위한 정보를 입력해주세요.",
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(24.dp))

                // 아이디 입력 필드
                TextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text("아이디") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                // 비밀번호 입력 필드
                TextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text("비밀번호") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(24.dp))

                // 회원가입 버튼
                Button(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("회원가입")
                }
            }
        }
    }

    @Composable
    fun RegisterScreen() {
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(223, 242, 235))
                    .padding(innerPadding)
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    // 제목 텍스트
                    Text(
                        text = "약속어때",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(63, 81, 181) // 색상은 이미지 참고하여 설정
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // 안내 텍스트
                    Text(
                        text = "서비스 사용을 위해 회원가입 해주세요.",
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    // 아이디 입력 필드
                    TextField(
                        value = "",
                        onValueChange = {},
                        placeholder = { Text("아이디") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // 비밀번호 입력 필드
                    TextField(
                        value = "",
                        onValueChange = {},
                        placeholder = { Text("비밀번호") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    // 회원가입 버튼
                    Button(
                        onClick = {},
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("회원가입")
                    }
                }
            }
        }
    }
}

