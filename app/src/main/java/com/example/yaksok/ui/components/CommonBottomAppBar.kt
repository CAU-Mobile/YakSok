package com.example.yaksok.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.yaksok.R

@Composable
fun CommonBottomAppBar(navController: NavController) {
    BottomAppBar (
        containerColor = Color.White,

        )
    {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        )
        { // 버튼을 가로로 배치하기 위해 Row 사용
            Button(
                onClick = {
                    navController.navigate("savedPlaces")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent // 버튼 배경을 투명하게 설정하여 이미지만 보이도록
                ),
                modifier = Modifier.size(100.dp) // 버튼 크기 설정
            ) {
                Image(
                    painter = painterResource(id = R.drawable.saved), // 버튼으로 사용할 이미지 리소스
                    contentDescription = "savedPlacesBtn", // 이미지 설명
                    modifier = Modifier.size(25.dp) // 이미지 크기 설정
                )
            }
            Button(
                onClick = {
                    navController.navigate("map")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent // 버튼 배경을 투명하게 설정하여 이미지만 보이도록
                ),
                modifier = Modifier.size(100.dp) // 버튼 크기 설정
            ) {
                Image(
                    painter = painterResource(id = R.drawable.home), // 버튼으로 사용할 이미지 리소스
                    contentDescription = "homeBtn", // 이미지 설명
                    modifier = Modifier.size(25.dp) // 이미지 크기 설정
                )
            }
            Button(
                onClick = {
                    navController.navigate("manageYaksok")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent // 버튼 배경을 투명하게 설정하여 이미지만 보이도록
                ),
                modifier = Modifier.size(100.dp) // 버튼 크기 설정
            ) {
                Image(
                    painter = painterResource(id = R.drawable.list), // 버튼으로 사용할 이미지 리소스
                    contentDescription = "manageYaksokBtn", // 이미지 설명
                    modifier = Modifier.size(25.dp) // 이미지 크기 설정
                )
            }
        }
    }
}