package com.example.yaksok

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.yaksok.feature.savePlace.SavePlaceViewModel
import com.example.yaksok.feature.savePlace.model.SavedPlace
import com.example.yaksok.query.Appointment
import com.example.yaksok.query.AppointmentQueryCoroutine
import com.example.yaksok.query.AuthQuery
import com.example.yaksok.ui.CreateYaksokPage
import com.example.yaksok.ui.ManageYaksokPage
import com.example.yaksok.ui.MapPage
import com.example.yaksok.ui.YaksokDetailPage
import com.example.yaksok.ui.YaksokViewModel
import com.example.yaksok.ui.components.CommonBottomAppBar
import com.example.yaksok.ui.components.CommonTopAppBar
import com.example.yaksok.ui.distance.viewModel.DistanceViewModel
import com.example.yaksok.ui.friend.AddFriendToYaksokPage
import com.example.yaksok.ui.friend.AddFriendViewModel
import com.example.yaksok.ui.friend.AddFriendsPage
import com.example.yaksok.ui.login.LoginPage
import com.example.yaksok.ui.login.LoginViewModel
import com.example.yaksok.ui.login.RegisterPage
import com.example.yaksok.ui.login.RegisterViewModel
import com.example.yaksok.ui.places.screen.PlaceDetailsScreen
import com.example.yaksok.ui.places.viewModel.PlacesViewModel
import com.example.yaksok.ui.routes.screen.GoogleMapScreen
import com.example.yaksok.ui.routes.viewModel.DirectionsViewModel
import com.example.yaksok.ui.savedPlaces.screen.SavedPlacesScreen
import com.example.yaksok.ui.theme.YakSokTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var placesViewModel: PlacesViewModel
    private lateinit var routeViewModel: DirectionsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            FirebaseApp.initializeApp(this)
        } catch (e: Exception) {
            Log.e("MainActivity", "Firebase 초기화 실패...", e)
        }
        val serviceLocator = (application as YakSokApplication).appContainer

        placesViewModel =
            serviceLocator.placesContainer.placesViewModelFactory.create(PlacesViewModel::class.java)

        routeViewModel =
            serviceLocator.directionsContainer.directionsViewModelFactory.create(DirectionsViewModel::class.java)

        val loginViewModel: LoginViewModel by viewModels()
        val checkCurrentUser = FirebaseAuth.getInstance().currentUser
        Log.d("MainActivity", "현재 로그인 상태: ${checkCurrentUser != null}")
        val friendList = listOf(
            "박수빈", "박예빈", "임결", "최지원", "이준우",
            "박수빈", "박예빈", "임결", "최지원", "이준우"
        ) //친구리스트 테스트용
//                val loginViewModel = LoginViewModel()

        val registerViewModel = RegisterViewModel()
        val addFriendViewModel = AddFriendViewModel()
        val yaksokViewModel = YaksokViewModel()
        val userId = AuthQuery.getCurrentUserId()
        val distanceViewModel = DistanceViewModel()
        val savePlaceViewModel = SavePlaceViewModel()

        enableEdgeToEdge()
        setContent {
            YakSokTheme {
                val navController = rememberNavController()

                LaunchedEffect(Unit) {
                    val currentUser = FirebaseAuth.getInstance().currentUser
                    if (currentUser != null) {
                        navController.navigate("map") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                }
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color.Transparent, // Scaffold 배경색 투명 설정
                    bottomBar = { CommonBottomAppBar(navController) },
                    topBar = {
                        CommonTopAppBar(
                            navController = navController,
                            goToAddFriendsPage = { navController.navigate("addFriends") },
                            viewModel = loginViewModel,
                            goToLoginPage = { navController.navigate("login") }
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
                                routeViewModel = routeViewModel,
                                placesViewmodel = placesViewModel,
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
                            ManageYaksokPage(
                                goToYaksokDetailPage = { appointmentId ->
                                    navController.navigate("yaksokDetail/$appointmentId")  // 화면 전환
                                },
                                viewModel = yaksokViewModel
                            )
                        }
                        composable("createYaksok") {
                            CreateYaksokPage(
                                goToAddFriendToYaksokPage = { navController.navigate("addFriendToYaksok") },
                                viewModel = yaksokViewModel,
                                secondeviewModel = addFriendViewModel,
                                placeViewModel = placesViewModel,
                                goToManageYaksokPage = { navController.navigate("manageYaksok") },
                                selectedFriends = addFriendViewModel.selectedFriends
                            )
                        }
                        composable("addFriendToYaksok") {
                            if (userId != null) {
                                AddFriendToYaksokPage(
                                    userId = userId,
                                    viewModel = addFriendViewModel
                                )
                            }
                        }
                        composable("addFriends") {
                            AddFriendsPage(
                                viewModel = addFriendViewModel
                            )
                        }
                        composable("yaksokDetail/{appointmentId}") { backStackEntry ->
                            val appointmentId = backStackEntry.arguments?.getString("appointmentId")
                            var appointment by remember { mutableStateOf<Appointment?>(null) }
                            var isLoading by remember { mutableStateOf(true) }

                            LaunchedEffect(appointmentId) {
                                if (appointmentId != null) {
                                    appointment =
                                        AppointmentQueryCoroutine.getAppointment(appointmentId)
                                    isLoading = false
                                }
                            }
                            if (isLoading) {
                                Text("Loading...", fontSize = 20.sp)
                            } else {
                                appointment?.let {
                                    YaksokDetailPage(
                                        appointment = it,
                                        viewModel = yaksokViewModel,
                                        distanceViewModel = distanceViewModel,
                                        savePlaceViewModel = savePlaceViewModel
                                    )
                                } ?: run {
                                    Text("Appointment not found", fontSize = 20.sp)
                                }
                            }
                        }
                        composable("savedPlaces") {
                            SavedPlacesScreen(
                                onPlaceClick = { savedPlace ->
                                    // 전체 SavedPlace 객체를 문자열로 변환하여 전달
                                    val placeJson = Uri.encode(Gson().toJson(savedPlace))
                                    navController.navigate("placeDetail/$placeJson")
                                }
                            )
                        }
                        composable(
                            route = "placeDetail/{placeJson}",
                            arguments = listOf(
                                navArgument("placeJson") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val placeJson = backStackEntry.arguments?.getString("placeJson")
                            // JSON 문자열을 다시 SavedPlace 객체로 변환
                            val place = placeJson?.let {
                                Gson().fromJson(Uri.decode(it), SavedPlace::class.java)
                            }

                            if (place != null) {
                                PlaceDetailsScreen(place = place)
                            } else {
                                Text("장소를 찾을 수 없습니다.", fontSize = 20.sp)
                            }
                        }
                        composable("mapScreen") {
                            GoogleMapScreen(
                                viewModel = routeViewModel,
                                navigateToInput = {
                                    navController.navigate("inputScreen")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
