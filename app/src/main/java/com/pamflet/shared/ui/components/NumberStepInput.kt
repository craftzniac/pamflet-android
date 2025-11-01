package com.pamflet.shared.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pamflet.R
import com.pamflet.shared.ui.theme.Gray300
import com.pamflet.shared.ui.theme.Gray900

@Composable
fun NumberStepInput(
    value: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    rawUpdate: (update: String) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(0.dp)
            .height(48.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.wrapContentWidth()
        ) {
            StepperButton(
                variant = StepperButtonVariant.Decrement,
                onAction = onDecrement,
            )
            Pipe()
            Box(modifier = Modifier.padding(horizontal = 4.dp)) {
                BasicTextField(
                    value = value.toString(),
                    onValueChange = { newStringValue ->
                        rawUpdate(newStringValue)
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                    decorationBox = { innerTextField ->
                        Row(
                            modifier = Modifier
                                .background(color = Color.White)
                                .width(45.dp)
                                .padding(4.dp)
                        ) {
                            innerTextField()
                        }
                    }
                )
            }
            Pipe()
            StepperButton(
                variant = StepperButtonVariant.Increment,
                onAction = onIncrement,
            )
        }
    }
}

@Composable
fun Pipe() {
    Spacer(
        modifier = Modifier
            .background(color = Gray300)
            .width(2.dp)
            .fillMaxHeight()
    )
}

enum class StepperButtonVariant {
    Increment,
    Decrement
}

@Composable
fun StepperButton(
    variant: StepperButtonVariant,
    onAction: () -> Unit
) {
    val corner = 4.dp
    IconButton(
        onClick = onAction,
        colors = IconButtonDefaults.iconButtonColors(containerColor = Color.White),
        modifier = Modifier
            .clip(
                shape = if (variant == StepperButtonVariant.Decrement) RoundedCornerShape(
                    topStart = corner,
                    bottomStart = corner
                ) else RoundedCornerShape(topEnd = corner, bottomEnd = corner)
            )
            .padding(horizontal = 2.dp, vertical = 4.dp)
    ) {
        Icon(
            painter = painterResource(
                id = if (variant == StepperButtonVariant.Decrement) R.drawable.minus else R.drawable.plus,
            ),
            contentDescription = "",
            modifier = Modifier.size(24.dp),
            tint = Gray900
        )
    }
}
