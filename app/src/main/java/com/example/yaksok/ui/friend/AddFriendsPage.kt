package com.example.yaksok.ui.friend

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFriendsPage (
    viewModel: AddFriendViewModel

) {
    var showSuccessDialog by remember { mutableStateOf(false) }
    var friendCode by remember { mutableStateOf("") } // TextField 상태 관리
    var friendId by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            text = "친구 추가",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color(58, 58, 58)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // 친구 코드 입력 TextField
        TextField(
            value = friendId,
            onValueChange = { friendId=it},
            placeholder = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "search",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "친구의 코드를 입력해주세요!",
                        color = Color.LightGray
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(240, 240, 240),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        // 친구 추가 버튼
        Button(
            onClick = { viewModel.addFriend(friendId)
                        showSuccessDialog = true},
            modifier = Modifier.align(Alignment.End),
            colors = ButtonDefaults.buttonColors(containerColor = Color(122, 178, 211)),
        ) {
            Text("친구 추가")
        }
    }

    // 성공 다이얼로그
    if (showSuccessDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = { Text("친구 추가") }, // 제목 수정
            text = { Text("친구가 성공적으로 추가되었습니다!") }, // 본문 수정
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(122, 178, 211))
                ) {
                    Text("확인")
                }
            }
        )
    }
}