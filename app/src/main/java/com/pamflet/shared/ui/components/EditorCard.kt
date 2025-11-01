package com.pamflet.shared.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pamflet.core.domain.Flashcard
import com.pamflet.shared.FontSize
import com.pamflet.shared.ui.renderer.WhichFaceChip

enum class CardFace {
    Back,
    Front
}

@Composable
fun FlippableCard(
    modifier: Modifier = Modifier,
    isFlipped: Boolean,
    isOutlinedCard: Boolean = false,  // regular Card not OutlinedCard
    front: @Composable () -> Unit,
    back: @Composable (modifier: Modifier) -> Unit
) {
    Column {
        Box(
            modifier = Modifier.wrapContentSize(),
            contentAlignment = Alignment.Center
        ) {
            val animatedFlip by animateFloatAsState(
                targetValue = if (isFlipped) 180f else 0f,
                label = "CardFlip",
                animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
            )
            val density = LocalDensity.current

            Card(
                border = if (isOutlinedCard) BorderStroke(
                    width = 1.dp,
                    color = Color.Gray
                ) else BorderStroke(width = 0.dp, color = Color.Transparent),
                elevation = if (isOutlinedCard) {
                    CardDefaults.outlinedCardElevation()
                } else CardDefaults.cardElevation(defaultElevation = 2.dp),
                colors = if (isOutlinedCard) {
                    CardDefaults.outlinedCardColors().copy(containerColor = Color.White)
                } else CardDefaults.cardColors().copy(containerColor = Color.White),
                modifier = modifier.then(
                    Modifier
                        .sizeIn(maxWidth = 350.dp, maxHeight = 450.dp)
                        .fillMaxSize()
                        .graphicsLayer {
                            rotationY = animatedFlip
                            transformOrigin = TransformOrigin.Center
                            cameraDistance = 20 * density.density
                        })
            ) {
                val isActuallyFlipped = animatedFlip > 90

                if (isActuallyFlipped) {
                    back(Modifier.graphicsLayer { rotationY = 180F })
                } else {
                    front()
                }
            }
        }
    }
}

@Composable
fun EditorCard(cardMutState: MutableState<Flashcard>, isFlipped: Boolean) {
    FlippableCard(
        isFlipped = isFlipped,
        isOutlinedCard = true,
        front = {
            Editor(
                cardFace = CardFace.Front,
                content = cardMutState.value.front,
                setContent = { newValue ->
                    cardMutState.value = cardMutState.value.copy(front = newValue)
                }
            )
        },
        back = { modifier ->
            Editor(
                modifier = modifier,
                cardFace = CardFace.Back,
                content = cardMutState.value.back,
                setContent = { newValue ->
                    cardMutState.value = cardMutState.value.copy(back = newValue)
                }
            )
        }
    )
}

@Composable
fun Editor(
    modifier: Modifier = Modifier,
    content: String,
    setContent: (newValue: String) -> Unit,
    cardFace: CardFace
) {
    BasicTextField(
        textStyle = TextStyle(fontSize = FontSize.Lg),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.None,
            autoCorrectEnabled = false,
        ),
        decorationBox = { innerTextField ->
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                WhichFaceChip(cardFace = cardFace)
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)) {
                    if (content.isEmpty()) {
                        Text("Write something ...", fontSize = 20.sp, color = Color.Gray)
                    }
                    innerTextField()
                }
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .border(width = 0.dp, color = Color.Transparent)
            .padding(0.dp)
            .then(modifier),
        value = content,
        onValueChange = { newValue ->
            setContent(newValue)
        }
    )
}