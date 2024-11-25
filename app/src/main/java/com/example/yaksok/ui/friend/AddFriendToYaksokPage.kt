package com.example.yaksok.ui.friend

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yaksok.R

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AddFriendToYaksokPage(
    userId: String,
    viewModel: AddFriendViewModel= viewModel()
) {

    LaunchedEffect(userId) {
        viewModel.loadFriends(userId)
    }
    val friendList by viewModel.friendList.collectAsState(emptyList())
    val loading = viewModel.loading
    val error = viewModel.error
    val isFriendAdded by viewModel.isFriendAdded.collectAsState(emptyMap())

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            Text(
                text = "친구 목록",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(58,58,58)
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "${friendList.size}명의 친구",
                fontSize = 15.sp,
                fontWeight = FontWeight.Light,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "약속에 추가할 친구를 선택하세요.",
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                color = Color.LightGray
            )
        }
        items(friendList) { friend -> //FriendList 에서 꺼내서 이름이랑 번호 할당.
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(50.dp)
                        .clip(RoundedCornerShape(25.dp))
                        .background(Color.Gray)
                        .shadow(10.dp),
                    contentDescription = "profileImg",
                    painter = painterResource(id = R.drawable.profile)
                )
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = friend.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Light,
                        color = Color(58,58,58)
                    )
                    Text(
                        text = friend.phoneNumber,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Light,
                        color = Color.Gray
                    )
                }
                Button(
                    onClick = {
                        if (isFriendAdded[friend.userCode] == true) {
                            viewModel.removeFriendFromYaksok(friend)
                        } else {
                            viewModel.addFriendToYaksok(friend)
                        }
                    },
                    modifier = Modifier.height(35.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(122, 178, 211))
                ) {
                    Text(
                        fontSize = 12.sp,
                        text =  if (isFriendAdded[friend.userCode] == true) "Complete" else "Add"
                    )
                }
            }
        }
    }
}
