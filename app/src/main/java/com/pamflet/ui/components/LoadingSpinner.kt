package com.pamflet.ui.components

import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pamflet.ui.theme.Gray200
import com.pamflet.ui.theme.Gray600


@Composable
fun LoadingSpinner() {
    CircularProgressIndicator(
        modifier = Modifier.width(24.dp),
        color = Gray600,
        trackColor = Gray200
    )
}