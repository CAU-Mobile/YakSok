package com.example.yaksok.ui.login

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yaksok.query.AuthQuery

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPage(
    goToRegisterPage: () -> Unit,
    goToMapPage: () -> Unit,
    viewModel: LoginViewModel,
    onLoginSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginMessage by remember { mutableStateOf("") }
    var showSuccessDialog = viewModel.showDialog.collectAsState()

    val isLoggedIn by viewModel.isLoggedIn.collectAsState()

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn == true) {
            onLoginSuccess()
        }
    }

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
//                   .background(Color.White)
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                // 제목 텍스트
                Text(
                    text = "약속어때",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(58, 58, 58) // 색상은 이미지 참고하여 설정
                )
                Spacer(modifier = Modifier.height(16.dp))

                // 안내 텍스트
                Text(
                    text = "서비스 사용을 위해 로그인 해주세요.",
                    fontSize = 16.sp,
                    color = Color.Gray,
                )
                Spacer(modifier = Modifier.height(24.dp))

                // 이메일 입력 필드
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = {
                        Text(
                            text = "이메일",
                            color = Color.LightGray,
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors( // 플레이스홀더 텍스트 색상
                        containerColor = Color(240, 240, 240),
                        focusedIndicatorColor = Color.Transparent, // 포커스된 상태의 인디케이터 색상 제거
                        unfocusedIndicatorColor = Color.Transparent // 포커스되지 않은 상태의 인디케이터 색상 제거
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 비밀번호 입력 필드
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = {
                        Text(
                            text = "비밀번호",
                            color = Color.LightGray,
                        )
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors( // 플레이스홀더 텍스트 색상
                        containerColor = Color(240, 240, 240),
                        focusedIndicatorColor = Color.Transparent, // 포커스된 상태의 인디케이터 색상 제거
                        unfocusedIndicatorColor = Color.Transparent // 포커스되지 않은 상태의 인디케이터 색상 제거
                    )
                )
                Spacer(modifier = Modifier.height(24.dp))

                // 로그인 버튼
                Button(
                    onClick = {
                        viewModel.login(email, password) // ViewModel 호출
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(122, 178, 211))
                ) {
                    Text("로그인")
                }

                // 회원가입 버튼
                Button(
                    onClick = goToRegisterPage,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Text(
                        text = "회원가입",
                        color = Color(122, 178, 211)
                    )
                }

                if (loginMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = loginMessage,
                        color = if (loginMessage.contains("로그인 성공")) Color.Blue else Color.Red
                    )
                }
            }
        }
    }

    //로그인 확인 메세지 추가~
    if (showSuccessDialog.value) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { viewModel.changeDialogState() },
            title = { Text("로그인 완료") },
            text = { Text("로그인 되었습니다.") },
            confirmButton = {
                Button(onClick = {
                    viewModel.changeDialogState()
                    // 로그인 성공 후 맵으로 이동
                    goToMapPage()
                }) {
                    Text("확인")
                }
            }
        )
    }
}