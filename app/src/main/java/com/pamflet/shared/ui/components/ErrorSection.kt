package com.pamflet.shared.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pamflet.shared.ui.theme.Gray600
import com.pamflet.shared.ui.theme.Gray900

@Composable
fun ErrorSection(
    modifier: Modifier = Modifier,
    message: String,
    detail: String = "",
    onAction: () -> Unit = {},
    actionLabel: String = "Retry",
    isFullscreen: Boolean = false
) {
    val fullscreenModifier = Modifier
    if (isFullscreen) {
        fullscreenModifier.fillMaxSize()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Transparent)
            .padding(8.dp)
            .then(fullscreenModifier)
            .then(modifier),
        verticalArrangement = Arrangement.spacedBy(4.dp, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            message,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Gray900,
            textAlign = TextAlign.Center
        )
        Text(
            detail,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Gray600,
            textAlign = TextAlign.Center
        )
        Button(
            onClick = onAction,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.height(48.dp)
        ) {
            Text(actionLabel)
        }
    }
}