package com.example.yaksok.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.yaksok.R
import com.example.yaksok.ui.login.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTopAppBar(
    navController: NavController,
    goToAddFriendsPage: () -> Unit,
    viewModel: LoginViewModel,
    goToLoginPage: () -> Unit
) {
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route

    TopAppBar(
        navigationIcon = {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Go Back"
                )
            }
        },
        title = { Spacer(modifier = Modifier.width(0.dp)) }, // 빈 공간으로 사실상 title 제거
        actions = {
            // 현재 페이지가 MapPage일 때만 버튼을 표시
            if (currentRoute == "map") {
                IconButton(onClick = goToAddFriendsPage) {
                    Icon(
                        painter = painterResource(id = R.drawable.addfriend), // 친구 추가 아이콘
                        contentDescription = "Add Friend"
                    )
                }
                IconButton(onClick = {
                    goToLoginPage()
                    viewModel.logout()
                }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = "LogOut"
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White // 배경색 설정
        )
    )
}
