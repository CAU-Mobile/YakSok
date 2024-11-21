package com.example.yaksok.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yaksok.ui.friend.AddFriendViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import java.sql.Time

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateYaksokPage(
    viewModel: YaksokViewModel
) {
    var name by remember { mutableStateOf("") }
    var details by remember { mutableStateOf(" ") }
    var geoPoint by remember { mutableStateOf("") }
    var timestamp by remember { mutableStateOf(" ") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "약속 만들기",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f), // 남은 공간을 채우도록 설정
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = name,
                onValueChange = {name = it},
                label = { Text("약속 이름") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color(185, 229, 232),
                    cursorColor = Color.Gray
                )
            )
            TextField(
                value = details,
                onValueChange = {details= it},
                label = { Text("약속 설명") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color(185, 229, 232),
                    cursorColor = Color.Gray
                )
            )
            // 나중에 지원님 코드 들어올때 반영해야 할곳 (장소 GeoPoint)
            TextField(
                value = geoPoint,
                onValueChange = {geoPoint=it},
                label = { Text("약속 장소") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color(185, 229, 232),
                    cursorColor = Color.Gray
                )
            )
            //시간 선택도 선택스크롤 페이지를 적용할지
            TextField(
                value = timestamp,
                onValueChange = {timestamp=it},
                label = { Text("약속 시간") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color(185, 229, 232),
                    cursorColor = Color.Gray
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                border = BorderStroke(2.dp, Color.Gray),
            ) {
                Text(
                    color = Color.Gray,
                    text = "친구 추가"
                )
            }
        }

        // '약속 만들기' 버튼을 화면 하단에 배치
        Button(
            onClick = {},
            modifier = Modifier
                .padding(vertical = 16.dp), // 버튼 상하 간격 조정
            contentPadding = PaddingValues(20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(122, 178, 211))
        ) {
            Text("약속 만들기")
        }
    }
}
