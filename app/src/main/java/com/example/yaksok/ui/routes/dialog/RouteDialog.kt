package com.example.yaksok.ui.routes.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.yaksok.ui.routes.viewModel.DirectionsViewModel

@Composable
fun RouteDialog(
    viewmodel: DirectionsViewModel,
    routes: List<String>,
    onIndexSelected: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedRouteIndex by rememberSaveable { mutableStateOf<Int?>(null) }
    viewmodel.refreshIndex()

    if (routes.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("경로 선택하기") },
            text = {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 300.dp)
                ) {
                    itemsIndexed(routes) { index, route ->
                        Text(
                            text = route,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedRouteIndex = index }
                                .background(if (selectedRouteIndex == index) Color.LightGray else Color.Transparent)
                                .padding(vertical = 8.dp, horizontal = 16.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        selectedRouteIndex?.let { onIndexSelected(it) }
                        onDismiss()
                    },
                    enabled = selectedRouteIndex != null,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(74, 98, 138))
                ) {
                    Text("선택완료")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        onDismiss()
                        viewmodel.refreshIndex()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(74, 98, 138))
                ) {
                    Text("취소")
                }
            }
        )
    }
}