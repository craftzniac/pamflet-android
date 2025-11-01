package com.pamflet.shared.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp

@Composable
fun TopAppBarTitleDescriptionText(text: String) {
    Text(
        text,
        color = Color(0xFF555555),
        fontSize = 12.sp,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        lineHeight = 12.sp
    )
}