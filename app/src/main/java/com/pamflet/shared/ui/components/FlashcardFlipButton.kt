package com.pamflet.shared.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.pamflet.R

@Composable
fun FlashcardFlipButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    PButton(
        modifier = modifier,
        onClick = onClick,
        variant = PButtonVariant.Secondary
    ) {
        Icon(
            painter = painterResource(R.drawable.arrows_counter_clockwise),
            contentDescription = "",
            modifier = Modifier.size(24.dp)
        )
        Text("Flip")
    }
}