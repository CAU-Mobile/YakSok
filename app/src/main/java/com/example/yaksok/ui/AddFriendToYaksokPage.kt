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

@Composable
fun AddFriendToYaksokPage(friendList: List<String>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = "친구 목록",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "${friendList.size}명의 친구",
                fontSize = 15.sp,
                fontWeight = FontWeight.Light,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "약속에 추가할 친구를 선택하세요.",
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                color = Color.LightGray
            )
        }
        items(friendList) { friend ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(50.dp)
                        .clip(RoundedCornerShape(25.dp))
                        .background(Color.Gray)
                        .shadow(10.dp),
                    contentDescription = "profileImg",
                    painter = painterResource(id = R.drawable.profile)
                )
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = friend,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Light,
                        color = Color.Black
                    )
                    Text(
                        text = "010-1234-5678",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Light,
                        color = Color.Gray
                    )
                }
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.height(35.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(122, 178, 211))
                ) {
                    Text(
                        fontSize = 12.sp,
                        text = "Add"
                    )
                }
            }
        }
    }
}
