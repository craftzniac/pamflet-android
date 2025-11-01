package com.pamflet.shared.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pamflet.shared.ui.theme.Gray200
import com.pamflet.shared.ui.theme.Gray600


@Composable
fun LoadingSpinner() {
    CircularProgressIndicator(
        modifier = Modifier.width(24.dp),
        color = Gray600,
        trackColor = Gray200
    )
}

@Composable
fun FullscreenLoadingSpinner() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        LoadingSpinner()
    }
}