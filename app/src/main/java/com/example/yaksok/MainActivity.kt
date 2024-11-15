package com.example.yaksok

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.yaksok.ui.AddFriendToYaksokPage
import com.example.yaksok.ui.AddFriendsPage
import com.example.yaksok.ui.CreateYaksokPage
import com.example.yaksok.ui.LoginPage
import com.example.yaksok.ui.ManageYaksokPage
import com.example.yaksok.ui.MapPage
import com.example.yaksok.ui.RegisterPage
import com.example.yaksok.ui.components.CommonBottomAppBar
import com.example.yaksok.ui.components.CommonTopAppBar
import com.example.yaksok.ui.theme.YakSokTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YakSokTheme {
                val navController = rememberNavController()
                val friendList = listOf("박수빈", "박예빈", "임결", "최지원", "이준우",
                    "박수빈", "박예빈", "임결", "최지원", "이준우")

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color.Transparent, // Scaffold 배경색 투명 설정
                    bottomBar = { CommonBottomAppBar(navController) },
                    topBar = { CommonTopAppBar(navController) }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,

                        startDestination = "addFriends",

                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("map") {
                            MapPage()
                        }
                        composable("login") {
                            LoginPage(goToRegisterPage = { navController.navigate("register") })
                        }
                        composable("register") {
                            RegisterPage(goToLoginPage = { navController.navigate("login") })
                        }
                        composable("manageYaksok") {
                            ManageYaksokPage()
                        }
                        composable("createYaksok") {
                            CreateYaksokPage()
                        }
                        composable("addFriendToYaksok") {
                            AddFriendToYaksokPage(friendList = friendList)
                        }
                        composable("addFriends") {
                            AddFriendsPage()
                        }
                    }
                }
            }
        }
    }
}
