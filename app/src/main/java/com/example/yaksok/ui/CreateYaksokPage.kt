package com.example.yaksok.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.util.Log
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yaksok.query.User
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import com.google.type.Date
import kotlinx.coroutines.flow.StateFlow
import java.util.Calendar

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateYaksokPage(
    viewModel: YaksokViewModel,
    goToAddFriendToYaksokPage: () -> Unit,
    selectedFriends: StateFlow<List<User>>,
    goToManageYaksokPage: () -> Unit
) {
    var showSuccessDialog by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var details by remember { mutableStateOf(" ") }
    var geoPoint by remember { mutableStateOf("") }
    var timestamp by remember { mutableStateOf<Timestamp?>(null) }
    var selectedDateTime by remember { mutableStateOf("YYYY-MM-DD HH:MM") }
    val context = LocalContext.current
    var showCalender by remember { mutableStateOf(false) }

    val calendar = Calendar.getInstance()
    var selectedDate by remember { mutableStateOf(calendar.timeInMillis) }
    var selectedHour by remember { mutableStateOf(calendar.get(Calendar.HOUR_OF_DAY)) }
    var selectedMinute by remember { mutableStateOf(calendar.get(Calendar.MINUTE)) }

    fun showTimePickerDialog() {
        val timePickerDialog = TimePickerDialog(
            context,
            { _: TimePicker, hourOfDay: Int, minute: Int ->
                // Update time selection
                selectedHour = hourOfDay
                selectedMinute = minute

                // Combine selected date and time to set timestamp
                calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
                calendar.set(Calendar.MINUTE, selectedMinute)

                selectedDate = calendar.timeInMillis
                timestamp = Timestamp(java.util.Date(selectedDate))
                selectedDateTime = String.format("%04d-%02d-%02d %02d:%02d",
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DAY_OF_MONTH),
                    selectedHour,
                    selectedMinute)
            },
            selectedHour,
            selectedMinute,
            true
        )
        timePickerDialog.show()
    }

    fun showDateTimePickerDialog() {
        // Date Picker Dialog
        val datePickerDialog = DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                calendar.set(year, month, dayOfMonth)
                showTimePickerDialog()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "약속 만들기",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color(58, 58, 58)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f), // 남은 공간을 채우도록 설정
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = goToAddFriendToYaksokPage,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                border = BorderStroke(2.dp, Color(122, 178, 211)),
            ) {
                Text(
                    color = Color.Black,
                    text = "친구 추가"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("약속 이름") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color(185, 229, 232),
                    cursorColor = Color.Gray
                )
            )
            TextField(
                value = details,
                onValueChange = { details = it },
                label = { Text("약속 설명") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color(185, 229, 232),
                    cursorColor = Color.Gray
                )
            )
            // 나중에 지원님 코드 들어올때 반영해야 할곳 (장소 GeoPoint)
            TextField(
                value = geoPoint,
                onValueChange = { geoPoint = it },
                label = { Text("약속 장소") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color(185, 229, 232),
                    cursorColor = Color.Gray
                )
            )
            TextField(
                value = selectedDateTime,
                onValueChange = { /* 값 변경할 필요 없음 */ },
                label = { Text("날짜 및 시간") },
                readOnly = true,
                modifier = Modifier
                    .clickable {
                        showCalender = true
                    }
                    .fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color(185, 229, 232),
                    cursorColor = Color.Gray
                )
            )
            Button(
                onClick = { showCalender = true
                    goToAddFriendToYaksokPage()},
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                border = BorderStroke(2.dp, Color.Gray),
            ) {
                Text(color = Color.Black, text= "시간 선택")
            }

            // '약속 만들기' 버튼을 화면 하단에 배치
            Button(
                onClick = {
                    showSuccessDialog = true
                    timestamp?.let {
                        viewModel.addYaksok(
                            name,
                            details,
                            geoPoint = GeoPoint(37.7749, -122.4194),
                            time = it,
                            friendList = selectedFriends
                        )
                    }
                },
                modifier = Modifier
                    .padding(vertical = 16.dp), // 버튼 상하 간격 조정
                contentPadding = PaddingValues(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(122, 178, 211))
            ) {
                Text("약속 만들기")
            }
        }
    }
    if (showCalender) {
        showDateTimePickerDialog()
        showCalender=false
    }

    if(showSuccessDialog){
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = { Text("약속 만들기") },
            text = { Text("약속이 성공적으로 만들어졌습니다.") },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        goToManageYaksokPage()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(122, 178, 211))
                ) {
                    Text("확인")
                }
            }
        )
    }
}