package com.pamflet.shared.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.pamflet.shared.ui.theme.Magenta
import com.pamflet.shared.ui.theme.Purple500
import com.pamflet.shared.ui.theme.Red400
import kotlin.math.roundToInt

data class LogoCard(
    val color: Color,
    val offset: Offset,
    val rotation: Float
)

val cards = listOf(
    LogoCard(
        color = Magenta,
        offset = Offset(-20f, -2f),
        rotation = -10f
    ),
    LogoCard(
        color = Purple500,
        offset = Offset(0f, 5f),
        rotation = 0f
    ),
    LogoCard(
        color = Red400,
        offset = Offset(20f, -5f),
        rotation = 10f
    ),
)

@Composable
fun Logo() {
    Box(
        modifier = Modifier.size(48.dp),
        contentAlignment = Alignment.Center
    ) {
        cards.map { card ->
            Card(
                shape = RoundedCornerShape(5f),
                colors = CardDefaults.cardColors(containerColor = card.color),
                modifier = Modifier
                    .size(16.dp, 20.dp)
                    .background(color = Color.Transparent)
                    .graphicsLayer(rotationZ = card.rotation)
                    .offset {
                        IntOffset(x = card.offset.x.roundToInt(), y = card.offset.y.roundToInt())
                    }
            ) { }
        }
    }
}