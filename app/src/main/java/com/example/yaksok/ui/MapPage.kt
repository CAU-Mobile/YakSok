package com.example.yaksok.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.yaksok.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapPage(
    goToCreateYaksokPage: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(223, 242, 235))
    ) {
        // 본문 텍스트
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Map Page",
                color = Color(58,58,58)
            )
        }

        // Floating Action Button (FAB)
        FloatingActionButton(
            onClick = goToCreateYaksokPage,
            containerColor = Color.Gray, // FAB 배경색
            contentColor = Color.White, // 아이콘 색상
            modifier = Modifier
                .align(Alignment.BottomEnd) // 우측 하단에 배치
                .padding(16.dp) // 화면 가장자리와의 여백
        ) {
            Icon(
                painter = painterResource(id = R.drawable.plus), // 플러스 아이콘 리소스
                contentDescription = "Add"
            )
        }
    }
}


@Preview
@Composable
fun MapPagePreview() {
    MapPage({})
}