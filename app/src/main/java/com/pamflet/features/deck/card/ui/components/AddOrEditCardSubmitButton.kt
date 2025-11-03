package com.pamflet.features.deck.card.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.pamflet.shared.ui.components.LoadingSpinner
import com.pamflet.shared.ui.components.PButton

@Composable
fun AddOrEditCardSubmitButton(
    onClick: () -> Unit,
    isSubmitting: Boolean = false,
    enabled: Boolean = true,
) {
    PButton(
        enabled = !isSubmitting,
        onClick = onClick,
    ) {
        if (isSubmitting) {
            LoadingSpinner()
        } else {
            Icon(
                painter = painterResource(com.pamflet.R.drawable.floppy_disk),
                contentDescription = ""
            )
            Text("Submit")
        }
    }
}