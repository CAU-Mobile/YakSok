package com.example.yaksok.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

val CustomTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = CustomFont,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    titleLarge = TextStyle(
        fontFamily = CustomFont,
        fontSize = 20.sp,
        lineHeight = 28.sp
    ),
    labelLarge = TextStyle(
        fontFamily = CustomFont,
        fontSize = 14.sp,
        lineHeight = 20.sp
    )
    // 추가 스타일 정의 가능
)
