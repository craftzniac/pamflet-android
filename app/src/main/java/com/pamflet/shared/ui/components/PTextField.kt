package com.pamflet.shared.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = label
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = value,
            onValueChange = { update -> onValueChange(update) },
            singleLine = true
        )
    }
}

