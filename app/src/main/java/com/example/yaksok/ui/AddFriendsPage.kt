package com.example.yaksok.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yaksok.R

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFriendsPage () {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "친구 추가",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = "",
            onValueChange = { },
            placeholder = {
                Row( // Row를 사용하여 아이콘과 텍스트를 가로 정렬
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "search",
                        modifier = Modifier
                            .size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp)) // 아이콘과 텍스트 사이 간격
                    Text(
                        text = "친구의 코드를 입력해주세요!",
                        color = Color.LightGray
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(240, 240, 240),
                focusedIndicatorColor = Color.Transparent, // 포커스된 상태의 인디케이터 색상 제거
                unfocusedIndicatorColor = Color.Transparent // 포커스되지 않은 상태의 인디케이터 색상 제거
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier.align(Alignment.End),
            colors = ButtonDefaults.buttonColors(containerColor = Color(122, 178, 211)),
        ) {
            Text("친구 추가")
        }
    }
}