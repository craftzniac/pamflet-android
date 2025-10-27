package com.pamflet.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pamflet.ui.theme.Gray600
import com.pamflet.ui.theme.Gray900

@Composable
fun ErrorSection(
    modifier: Modifier = Modifier,
    message: String,
    detail: String = "",
    onRetry: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
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
            onClick = onRetry,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.height(48.dp)
        ) {
            Text("Retry")
        }
    }
}