package com.example.yaksok

import android.os.Bundle
import com.example.yaksok.ui.login.LoginPage
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
import com.example.yaksok.query.AuthQuery
import com.example.yaksok.ui.friend.AddFriendToYaksokPage
import com.example.yaksok.ui.friend.AddFriendsPage
import com.example.yaksok.ui.CreateYaksokPage
import com.example.yaksok.ui.ManageYaksokPage
import com.example.yaksok.ui.MapPage
import com.example.yaksok.ui.SavedPlacesPage
import com.example.yaksok.ui.YaksokDetailPage
import com.example.yaksok.ui.YaksokViewModel
import com.example.yaksok.ui.login.RegisterPage
import com.example.yaksok.ui.components.CommonBottomAppBar
import com.example.yaksok.ui.components.CommonTopAppBar
import com.example.yaksok.ui.friend.AddFriendViewModel
import com.example.yaksok.ui.login.LoginViewModel
import com.example.yaksok.ui.login.RegisterViewModel
import com.example.yaksok.ui.theme.YakSokTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YakSokTheme {
                val navController = rememberNavController()
                val friendList = listOf("박수빈", "박예빈", "임결", "최지원", "이준우",
                    "박수빈", "박예빈", "임결", "최지원", "이준우") //친구리스트 테스트용
                val loginViewModel = LoginViewModel()
                val registerViewModel= RegisterViewModel()
                val AddFriendViewModel= AddFriendViewModel()
                val YaksokViewModel=YaksokViewModel()
                val userId=AuthQuery.getCurrentUserId()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color.Transparent, // Scaffold 배경색 투명 설정
                    bottomBar = { CommonBottomAppBar(navController) },
                    topBar = {
                        CommonTopAppBar(
                            navController = navController,
                            goToAddFriendsPage = { navController.navigate("addFriends") }
                        )
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,

                        startDestination = "login",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("map") {
                            MapPage(
                                goToCreateYaksokPage = { navController.navigate("createYaksok") }
                            )
                        }
                        composable("login") {
                            LoginPage(
                                goToRegisterPage = { navController.navigate("register") },
                                viewModel = loginViewModel,
                                onLoginSuccess = { navController.navigate("map") },//로그인 성공시 이동
                                goToMapPage = { navController.navigate("map") }
                            )
                        }
                        composable("register") {
                            RegisterPage(
                                goToLoginPage = { navController.navigate("login") },
                                viewModel = registerViewModel
                            )
                        }
                        composable("manageYaksok") {
                            ManageYaksokPage(goToYaksokDetailPage = { navController.navigate("yaksokDetail") })
                        }
                        composable("createYaksok") {
                            CreateYaksokPage(
                                goToAddFriendToYaksokPage ={navController.navigate("addFriendToYaksok")},
                                viewModel = YaksokViewModel,
                                selectedFriends =  AddFriendViewModel.selectedFriends
                            )
                        }
                        composable("addFriendToYaksok") {
                            if (userId != null) {
                                AddFriendToYaksokPage(
                                    userId=userId,
                                    viewModel = AddFriendViewModel
                                )
                            }
                        }
                        composable("addFriends") {
                            AddFriendsPage(
                                viewModel = AddFriendViewModel
                            )
                        }
                        composable("yaksokDetail") {
                            YaksokDetailPage()
                        }
                        composable("savedPlaces") {
                            SavedPlacesPage()
                        }
                    }
                }
            }
        }
    }
}
