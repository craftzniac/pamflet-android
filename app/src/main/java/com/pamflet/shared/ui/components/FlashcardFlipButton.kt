package com.pamflet.shared.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.pamflet.R

@Composable
fun FlashcardFlipButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        onClick = onClick
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(
                4.dp,
                alignment = Alignment.CenterHorizontally
            )
        ) {
            Icon(
                painter = painterResource(R.drawable.arrows_counter_clockwise),
                contentDescription = ""
            )
            Text("flip")
        }
    }
}