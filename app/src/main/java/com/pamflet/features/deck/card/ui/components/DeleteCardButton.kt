package com.pamflet.features.deck.card.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.pamflet.R
import com.pamflet.shared.ui.components.LoadingSpinner
import com.pamflet.shared.ui.theme.Red500

@Composable
fun DeleteCardButton(
    onAction: () -> Unit,
    isSubmitting: Boolean
){
    IconButton(
        onClick = onAction,
        enabled = !isSubmitting
    ) {
        if (isSubmitting) {
            LoadingSpinner()
        } else {
            Icon(
                painter = painterResource(R.drawable.trash_can),
                contentDescription = "trash can",
                tint = Red500
            )
        }
    }
}
