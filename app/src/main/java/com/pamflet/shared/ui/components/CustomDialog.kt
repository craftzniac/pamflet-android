package com.pamflet.shared.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pamflet.shared.ui.theme.Gray300
import com.pamflet.shared.ui.theme.Gray900

@Composable
fun CustomDialog(
    title: String,
    description: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    isSubmitting: Boolean,
    icon: (@Composable () -> Unit)? = null
) {
    AlertDialog(
        containerColor = Color.White,
        icon = icon
            ?: @Composable {
                Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.fillMaxWidth()) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "",
                        modifier = Modifier.size(64.dp)
                    )
                }
            },
        title = {
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = title, fontSize = 24.sp)
            }
        },
        text = {
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = description, fontSize = 16.sp)
            }
        },
        onDismissRequest = onCancel,
        dismissButton = {
            Button(
                enabled = !isSubmitting,
                onClick = onCancel,
                modifier = Modifier
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Gray300),
            ) {
                Text("Cancel", color = Gray900)
            }
        },
        confirmButton = {
            Button(
                enabled = !isSubmitting,
                onClick = onConfirm,
                modifier = Modifier
                    .height(48.dp)
                    .width(105.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (isSubmitting) {
                        LoadingSpinner()
                    } else {
                        Text("Confirm", color = Gray900)
                    }
                }
            }
        }
    )
}
