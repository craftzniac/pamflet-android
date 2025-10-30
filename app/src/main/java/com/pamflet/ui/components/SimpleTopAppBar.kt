package com.pamflet.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.TextStyle
import com.pamflet.ui.theme.Gray900

val topAppBarTitleTextStyle = TextStyle(
    fontSize = 16.sp,
    fontWeight = FontWeight.Medium,
    lineHeight = 10.sp,
    color = Gray900
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleTopAppBar(
    title: String,
    isShowPamfletLogo: Boolean = false,
    onNavigateBack: (() -> Unit)? = null,
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
        navigationIcon = {
            if (isShowPamfletLogo) {
                Logo()
            } else if (onNavigateBack != null) { // show back navigation button
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        painter = painterResource(com.pamflet.R.drawable.arrow_left),
                        contentDescription = "",
                        modifier = Modifier.size(24.dp),
                        tint = Gray900
                    )
                }
            }
        },
        title = {
            Row(
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(text = title, style = topAppBarTitleTextStyle)
            }
        }
    )
}