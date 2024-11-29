package com.example.yaksok.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.yaksok.R
import com.example.yaksok.ui.routes.dialog.RouteDialog
import com.example.yaksok.ui.routes.screen.GoogleMapScreen
import com.example.yaksok.ui.routes.screen.RouteInputScreen
import com.example.yaksok.ui.routes.viewModel.DirectionsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapPage(
    routeViewModel: DirectionsViewModel,
    goToCreateYaksokPage: () -> Unit
) {
    val navController = rememberNavController()
    var showDialog by remember { mutableStateOf(false) }
    var origin by remember { mutableStateOf("") }
    var destination by remember { mutableStateOf("") }
    var timeState by remember { mutableStateOf(0) }
    var selectedHourMinute by remember { mutableStateOf(Pair(0, 0)) }

    val routes by routeViewModel.routeSelectionText.collectAsState(initial = emptyList())

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(223, 242, 235))
    ) {

        NavHost(navController = navController, startDestination = "inputScreen") {
            composable("inputScreen") {
                RouteInputScreen(
                    origin = origin,
                    destination = destination,
                    onOriginChange = { origin = it },
                    onDestinationChange = { destination = it },
                    onTimeChange = { currentTimeState, selectedHM ->
                        timeState = currentTimeState
                        selectedHourMinute = selectedHM
                    },
                    onSearchClicked = { origin, destination, mode, timeSelectedState, selectedHourMinute ->
                        routeViewModel.setEverything(
                            origin,
                            destination,
                            mode,
                            timeSelectedState,
                            selectedHourMinute
                        )
                        showDialog = true
                    }
                )
            }
            composable("mapScreen") {
                GoogleMapScreen(
                    viewModel = routeViewModel,
                    onBackPressed = { navController.popBackStack() }
                )
            }
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
        if (showDialog) {
            RouteDialog(
                viewmodel = routeViewModel,
                routes = routes,
                onIndexSelected = { index ->
                    routeViewModel.setSelectedRouteIndex(index)
                    routeViewModel.selectRoute(index)
                    showDialog = false
                    navController.navigate("mapScreen")
                },
                onDismiss = { showDialog = false }
            )
        }
    }

    LaunchedEffect(routes) {
        if (routes.isNotEmpty()) {
            showDialog = true
        }
    }
}