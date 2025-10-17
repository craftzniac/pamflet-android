package com.example.pamflet.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleTopAppBar(
    title: String,
    isShowPamfletLogo: Boolean = false
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
        navigationIcon = {},
        title = {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(16.dp)
            ) {
                if (isShowPamfletLogo) {
                    Logo()
                    Spacer(modifier = Modifier.width(width = 16.dp))
                }
                Text(title, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
        }
    )
}