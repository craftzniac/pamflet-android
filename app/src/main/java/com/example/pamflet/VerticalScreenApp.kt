package com.example.pamflet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun VerticalScreenApp() {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .background(Color(0xFFADADFF))
                    .padding(16.dp),
            ) {
                Text("Text 2", fontSize = 18.sp)
                Text("Text 3", fontSize = 18.sp)
                Text("Text 4", fontSize = 18.sp)
                Text("Text 4", fontSize = 18.sp)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(onClick = {

                }) {
                    Text("hello me")
                }

            }
        }

    }
}

@Preview(
)
@Composable
fun VerticalScreenAppPreview() {
    VerticalScreenApp()
}