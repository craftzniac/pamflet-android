package com.pamflet.ui.screens.managedecks.addoreditdeck

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pamflet.ui.components.PTextField

@Composable
fun DeckForm(
    modifier: Modifier = Modifier,
    deckName: String,
    updateDeckName: (update: String) -> Unit,
    onSubmit: () -> Unit,
    isEnableSubmitBtn: Boolean,
    submitBtnLabel: String,
    isSubmitting: Boolean = false
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        PTextField(
            value = deckName, onValueChange = updateDeckName, label = "Deck name"
        )
        Button(
            onClick = onSubmit,
            enabled = isEnableSubmitBtn,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.height(48.dp)
        ) {
            if (isSubmitting) {
                CircularProgressIndicator( modifier = Modifier.size(12.dp) )
            } else {
                Text(
                    submitBtnLabel,
                    fontSize = 16.sp,
                )
            }
        }
    }
}