package com.example.pamflet.renderer.components

import android.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                            alpha = if (isShowResultMutState.value) 0.5F else 1F
                        )
                        .fillMaxWidth()
                        .background(
                            color = Color(0xFFDDDDDD),
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
                        RadioButton(
                            selected = selectedIndexMutState.value == index.toUInt(),
                            onClick = null
                        )
                        Text(option, modifier = Modifier.fillMaxWidth())
                    }
                }
            }
        }

        if (isShowResultMutState.value) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = if (isCorrectMutState.value == true) Color(0xFFEEFFEE) else Color(
                            0xFFFFEEEE
                        ),
                        shape = RoundedCornerShape(12.dp),
                    )
                    .border(
                        shape = RoundedCornerShape(12.dp),
                        width = 1.dp,
                        color = if (isCorrectMutState.value == true) Color(0xFFAAFFAA) else Color(
                            0xFFFFAAAA
                        )
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
                        color = if (isCorrectMutState.value == true) Color(0xFF008800) else Color(
                            0xFF880000
                        ),
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

//                Text(
//                    modifier = Modifier.fillMaxWidth(),
//                    text = "here is some description as to why the answer is ... the answer"
//                )
            }
        } else {
            Button(
                colors = ButtonColors(
                    containerColor = Color(0xFFDDDDDD),
                    contentColor = Color.Black,
                    disabledContentColor = Color(0xFFAAAAAA),
                    disabledContainerColor = Color(0xFFEEEEEE)
                ),
                onClick = {  // check answer
                    isShowResultMutState.value = true
                    isCorrectMutState.value = selectedIndexMutState.value == el.correct
                }
            ) {
                Text("verify")
            }
        }
    }
}

@Composable
fun PMultichoiceMultiSelect(el: Element.Multichoice.MultiSelect) {

}
