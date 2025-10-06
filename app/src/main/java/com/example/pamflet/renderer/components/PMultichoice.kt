package com.example.pamflet.renderer.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pamflet.FontSize
import com.example.pamflet.parseColor
import com.example.pamflet.ui.theme.Green100
import com.example.pamflet.ui.theme.Green200
import com.example.pamflet.ui.theme.Green300
import com.example.pamflet.ui.theme.Green700
import com.example.pamflet.ui.theme.Red100
import com.example.pamflet.ui.theme.Red200
import com.example.pamflet.ui.theme.Red300
import com.example.pamflet.ui.theme.Red700
import pamflet.parser.Element

@Composable
fun PMultichoice(el: Element.Multichoice) {
    when (el) {
        is Element.Multichoice.SingleSelect -> PMultichoiceSingleSelect(el)
        is Element.Multichoice.MultiSelect -> PMultichoiceMultiSelect(el)
    }
}

@Composable
fun PMultichoiceSingleSelect(el: Element.Multichoice.SingleSelect) {
    val selectedIndexMutState = remember { mutableStateOf<UInt?>(null) }

    val isShowResultMutState = remember { mutableStateOf(false) }
    val isCorrectMutState = remember { mutableStateOf<Boolean?>(null) }

    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.selectableGroup()
        ) {
            el.options.forEachIndexed { index, option ->
                Surface(
                    color = Color.Transparent,
                    modifier = Modifier
                        .alpha(
                            alpha = if (isShowResultMutState.value) 0.8F else 1F
                        )
                        .fillMaxWidth()
                        .background(
                            color = if (isShowResultMutState.value) {
                                if (selectedIndexMutState.value == el.correct) {   // if correct answer was chosen, color only the background of the correct answer
                                    if (el.correct == index.toUInt()) {
                                        parseColor(str = el.colorCorrect, default = Green200)
                                    } else {
                                        Color(0xFFDDDDDD)
                                    }
                                } else {  // if an incorrect answer was chosen, color both correct and incorrect answers accordingly
                                    if (el.correct == index.toUInt()) {
                                        parseColor(str = el.colorCorrect, default = Green200)
                                    } else {
                                        parseColor(str = el.colorIncorrect, default = Red200)
                                    }
                                }
                            } else Color(0xFFDDDDDD),
                            shape = RoundedCornerShape(percent = 100),
                        )
                        .clip(shape = RoundedCornerShape(percent = 100))
                        .selectable(
                            enabled = !isShowResultMutState.value,
                            selected = selectedIndexMutState.value == index.toUInt(),
                            onClick = {
                                selectedIndexMutState.value = index.toUInt()
                            },
                            role = Role.RadioButton,
                            indication = ripple(bounded = false),
                            interactionSource = remember { MutableInteractionSource() }
                        )
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp, 8.dp)
                    ) {
                        val color = parseColor(el.color, default = Color.Black)
                        RadioButton(
                            selected = selectedIndexMutState.value == index.toUInt(),
                            onClick = null,
                            colors = RadioButtonDefaults.colors(
                                unselectedColor = color
                            )
                        )
                        Text(
                            option,
                            modifier = Modifier.fillMaxWidth(),
                            color = color,
                            fontSize = FontSize.parse(el.fontSize)
                        )
                    }
                }
            }
        }

        if (isShowResultMutState.value) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = if (isCorrectMutState.value == true) Green100 else Red100,
                        shape = RoundedCornerShape(12.dp),
                    )
                    .border(
                        shape = RoundedCornerShape(12.dp),
                        width = 1.dp,
                        color = if (isCorrectMutState.value == true) Green300 else Red300
                    )
                    .clip(shape = RoundedCornerShape(12.dp))
                    .padding(16.dp, 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .weight(1F),
                        text = if (isCorrectMutState.value == true) "Correct!" else "Wrong!",
                        color = if (isCorrectMutState.value == true) Green700 else Red700,
                        fontSize = 18.sp
                    )
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "close",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable(onClick = {
                                isShowResultMutState.value = false
                            })
                            .clip(shape = RoundedCornerShape(percent = 100)),
                        tint = Color.Black
                    )
                }

                if (el.explanation.isNotEmpty()) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = el.explanation
                    )
                }
            }
        } else {
            VerifySelectionsBtn(enabled = selectedIndexMutState.value != null) { // check answer
                isShowResultMutState.value = true
                isCorrectMutState.value = selectedIndexMutState.value == el.correct
            }
        }
    }
}

@Composable
fun VerifySelectionsBtn(enabled: Boolean, onClick: () -> Unit) {
    Button(
        enabled = enabled,
        colors = ButtonColors(
            containerColor = Color(0xFFDDDDDD),
            contentColor = Color.Black,
            disabledContainerColor = Color(0xFFDDDDDD),
            disabledContentColor = Color(0xFF555555),
        ),
        onClick = onClick
    ) {
        Text("verify")
    }
}

@SuppressLint("MutableCollectionMutableState")
@Composable
fun PMultichoiceMultiSelect(el: Element.Multichoice.MultiSelect) {
    val selectedIndexesMutState = remember { mutableStateListOf<UInt>() }

    val isShowResultMutState = remember { mutableStateOf(false) }

    fun isOptionSelected(index: Int): Boolean {
        return selectedIndexesMutState.contains(index.toUInt())
    }

    fun isOptionCorrect(index: Int): Boolean {
        return el.correct.contains(index.toUInt())
    }

    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.selectableGroup()
        ) {

            fun toggleSelectionOnOption(index: Int) {
                if (isOptionSelected(index)) {
                    selectedIndexesMutState.remove(index.toUInt())
                } else {
                    selectedIndexesMutState.add(index.toUInt())
                }
            }

            el.options.forEachIndexed { index: Int, option ->
                Surface(
                    color = Color.Transparent,
                    modifier = Modifier
                        .alpha(
                            alpha = if (isShowResultMutState.value) 0.8F else 1F
                        )
                        .fillMaxWidth()
                        .background(
                            color = if (isShowResultMutState.value) {
                                if (isOptionSelected(index)) {
                                    if (isOptionCorrect(index)) {
                                        parseColor(el.colorCorrect, default = Green200)
                                    } else {
                                        parseColor(el.colorIncorrect, default = Red200)
                                    }
                                } else {
                                    if (isOptionCorrect(index)) {
                                        parseColor(el.colorCorrect, default = Green200)
                                    } else {
                                        Color(0xFFDDDDDD)
                                    }
                                }
                            } else Color(0xFFDDDDDD),
                            shape = RoundedCornerShape(percent = 100),
                        )
                        .clip(shape = RoundedCornerShape(percent = 100))
                        .clickable(
                            enabled = !isShowResultMutState.value,
                            onClick = { toggleSelectionOnOption(index) },
                            role = Role.Checkbox
                        )
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp, 8.dp)
                    ) {
                        val color = parseColor(el.color, default = Color.Black)
                        Checkbox(
                            checked = isOptionSelected(index),
                            onCheckedChange = null,
                            colors = CheckboxDefaults.colors(uncheckedColor = color)
                        )
                        Text(
                            option,
                            modifier = Modifier.fillMaxWidth(),
                            color = color,
                            fontSize = FontSize.parse(el.fontSize)
                        )
                    }
                }
            }
        }

        val selectedCorrectCount = selectedIndexesMutState.filter { it in el.correct }.size
        val isMoreThan50PercentCorrect = selectedCorrectCount > el.correct.size.div(2)

        if (isShowResultMutState.value) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = if (isMoreThan50PercentCorrect) Green100 else Red100,
                        shape = RoundedCornerShape(12.dp),
                    )
                    .border(
                        shape = RoundedCornerShape(12.dp),
                        width = 1.dp,
                        color = if (isMoreThan50PercentCorrect) Green300 else Red300
                    )
                    .clip(shape = RoundedCornerShape(12.dp))
                    .padding(16.dp, 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .weight(1F),
                        text = "You correctly got $selectedCorrectCount of ${el.correct.size}",
                        color = if (isMoreThan50PercentCorrect) Green700 else Red700,
                        fontSize = 18.sp
                    )
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "close",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable(onClick = {
                                isShowResultMutState.value = false
                            })
                            .clip(shape = RoundedCornerShape(percent = 100)),
                        tint = Color.Black
                    )
                }

                if (el.explanation.isNotEmpty()) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = el.explanation
                    )
                }
            }
        } else {
            VerifySelectionsBtn(enabled = selectedIndexesMutState.isNotEmpty()) {
                isShowResultMutState.value = true
            }
        }
    }
}
