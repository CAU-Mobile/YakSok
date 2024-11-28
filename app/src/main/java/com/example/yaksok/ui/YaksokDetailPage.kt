package com.example.yaksok.ui
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yaksok.R

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.tooling.preview.Preview
import com.example.yaksok.query.Appointment
import com.example.yaksok.query.FriendsQuery
import com.example.yaksok.query.FriendsQueryCoroutine
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YaksokDetailPage(
    appointment: Appointment,
    viewModel: YaksokViewModel
) {
    LaunchedEffect(appointment.memberIds) {
        appointment.memberIds.forEach { memberId ->
            viewModel.loadFriendNumber(memberId)

            viewModel.loadFriendName(memberId)
        }
    }

    val friendNumbers by viewModel.friendNumbers.observeAsState(emptyMap())
    val friendNames by viewModel.friendNames.observeAsState(emptyMap())

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(25.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "약속 상세",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(58, 58, 58)
            )
        }
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(223, 242, 235))
                    .padding(25.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = appointment.name,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(58, 58, 58)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = DateTimeFormatter
                            .ofPattern("MM월 dd일\r\nHH시 mm분")
                            .withZone(ZoneId.of("Asia/Seoul"))
                            .format(appointment.time.toDate().toInstant()),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Light,
                        color = Color(122, 178, 211)
                    )
                }
                Divider(
                    color = Color(122, 178, 211),      // 선 색상
                    thickness = 1.dp,        // 선 두께
                )
                Spacer(modifier = Modifier.height(16.dp))
                Column( modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(5.dp))
                ) {
                    Text(
                        text = "낫투두 낫토앤바 용산점"
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Image(
                        painter = painterResource(id = R.drawable.yaksok_place_img),
                        contentDescription = "약속 장소",
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
//                    .clip(RoundedCornerShape(5.dp))
                        .border(BorderStroke(1.dp, Color(122, 178, 211)))
                        .padding(16.dp)
                ) {
                    Text(
                        text = appointment.details,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraLight,
                        color = Color(58,58,58)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "약속 참여자",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(58,58,58)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        items(appointment.memberIds) { memberId ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = "약속 참여자",
                    modifier = Modifier.size(30.dp)
                )
                val friendName = friendNames[memberId] ?: "이름 가져오는중"
                Text(
                    text = friendName,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Light,
                    color = Color(58, 58, 58)
                )
                Spacer(modifier = Modifier.width(8.dp))

                val friendNumber = friendNumbers[memberId] ?: "번호 가져오는 중"
                // 친구 번호 표시
                Text(
                    text = friendNumber,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Light,
                    color = Color(58, 58, 58)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
        }
    }
}
