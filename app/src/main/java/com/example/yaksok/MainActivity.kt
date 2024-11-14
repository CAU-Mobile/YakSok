package com.example.yaksok

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.yaksok.ui.LoginPage
import com.example.yaksok.ui.MapPage
import com.example.yaksok.ui.RegisterPage
import com.example.yaksok.ui.components.CommonBottomAppBar
import com.example.yaksok.ui.theme.YakSokTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YakSokTheme {
                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { CommonBottomAppBar(navController) }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "login",
                        modifier = Modifier.padding(innerPadding) // innerPadding 적용
                    ) {
                        composable("map") { MapPage() }
                        composable("login") { LoginPage(goToRegisterPage = { navController.navigate("register") }) }
                        composable("register") { RegisterPage(goToLoginPage = { navController.navigate("login") }) }
                        // 필요한 다른 페이지 추가
                    }
                }
            }
        }
    }
}


