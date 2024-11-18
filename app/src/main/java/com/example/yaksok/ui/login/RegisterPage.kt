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
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import androidx.lifecycle.ViewModel
import com.example.yaksok.query.AuthQuery

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterPage(
    goToLoginPage: () -> Unit,
    viewModel: RegisterViewModel
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var registerMessage by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var showSuccessDialog by remember { mutableStateOf(false) }

    val registerStatus by viewModel.registerStatus.collectAsState()
    val registerError by viewModel.registerError.collectAsState()
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
                    value = email,
                    onValueChange = {email=it},
                    placeholder = {
                        Text(
                            text = "이메일",
                            color = Color.LightGray
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
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
                    value = password,
                    onValueChange = {password=it},
                    placeholder = {
                        Text(
                            text = "비밀번호",
                            color = Color.LightGray
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
                Spacer(modifier = Modifier.height(12.dp))
                // 선 긋기
                Divider(
                    color = Color.LightGray,      // 선 색상
                    thickness = 1.dp,        // 선 두께
                )
                Spacer(modifier = Modifier.height(12.dp))
                // 이름 입력 필드
                TextField(
                    value = name,
                    onValueChange = {name=it},
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
                    value = phoneNumber,
                    onValueChange = {phoneNumber=it},
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
                    onClick = {
                        viewModel.register(email, password, name, phoneNumber)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(122, 178, 211))
                ) {
                    Text("회원가입")
                }
                if (registerMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = registerMessage,
                        color = if (registerMessage.contains("회원가입 성공")) Color.Green else Color.Red
                    )
                }
            }
        }
    }
    //회원가입 확인 메세지 추가~
    if (showSuccessDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = { Text("회원가입 완료") },
            text = { Text("회원가입이 성공적으로 완료되었습니다!") },
            confirmButton = {
                Button(onClick = {
                    showSuccessDialog = false
                    goToLoginPage() // 회원가입 성공 후 로그인 페이지로 이동
                }) {
                    Text("확인")
                }
            }
        )
    }

    registerStatus?.let { isSuccess ->
        if (isSuccess) {
            showSuccessDialog = true
            viewModel.clearRegisterState() // 상태 초기화
        }
    }
}