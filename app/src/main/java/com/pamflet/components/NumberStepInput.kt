package com.pamflet.components

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
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pamflet.ui.theme.Gray300
import com.pamflet.ui.theme.Gray900

@Composable
fun NumberStepInput(
    valueMutState: MutableIntState,
    step: Int = 1,
    minValue: Int = 0,
    maxValue: Int = 500,
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
                step,
                valueMutState,
                minValue,
                maxValue
            )
            Pipe()
            Box(modifier = Modifier.padding(horizontal = 4.dp)) {
                BasicTextField(
                    value = valueMutState.intValue.toString(),
                    onValueChange = { newStringValue ->
                        if (newStringValue.isEmpty()) {
                            valueMutState.intValue = 0
                            return@BasicTextField
                        }
                        val numVal = parseToNaturalNumBetweenZeroAndOneThousand(newStringValue)
                        if (numVal != null && numVal >= minValue && numVal <= maxValue) {
                            valueMutState.intValue = numVal
                        }
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
                                .width(36.dp)
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
                step,
                valueMutState,
                minValue, maxValue
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
    step: Int = 1,
    valueMutState: MutableIntState,
    minValue: Int,
    maxValue: Int
) {
    val incrementByStep = {
        val newValue = valueMutState.intValue + step
        // do not allow the state to be set above the maximum value
        if (newValue > maxValue) valueMutState.intValue = maxValue else valueMutState.intValue =
            newValue
    }

    val decrementByStep = {
        val newValue = valueMutState.intValue - step
        // do not allow the state to be set below the minimum value
        if (newValue < minValue) valueMutState.intValue = minValue else valueMutState.intValue =
            newValue
    }

    val corner = 4.dp
    IconButton(
        onClick = if (variant == StepperButtonVariant.Decrement) decrementByStep else incrementByStep,
        colors = IconButtonDefaults.iconButtonColors(containerColor = Color.White),
        modifier = Modifier
            .clip(
                shape = if (variant == StepperButtonVariant.Decrement) RoundedCornerShape(
                    topStart = corner,
                    bottomStart = corner
                ) else RoundedCornerShape(topEnd = corner, bottomEnd = corner)
            )
            .padding(4.dp)
    ) {
        Icon(
            painter = painterResource(
                id = if (variant == StepperButtonVariant.Decrement) com.pamflet.R.drawable.minus else com.pamflet.R.drawable.plus,
            ),
            contentDescription = "",
            modifier = Modifier.size(24.dp),
            tint = Gray900
        )
    }
}

fun parseToNaturalNumBetweenZeroAndOneThousand(value: String): Int? {
    try {
        val newUnit = value.toUInt()
        if (newUnit == 0u) {
            throw NumberFormatException()
        }
        return newUnit.toInt()
    } catch (err: NumberFormatException) {
        return null
    }
}