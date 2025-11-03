package com.pamflet.shared.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pamflet.shared.ui.theme.Gray200
import com.pamflet.shared.ui.theme.Gray900
import com.pamflet.shared.ui.theme.Purple500

enum class PButtonVariant {
    Primary,
    Secondary
}

@Composable
fun getVariantColors(variant: PButtonVariant): ButtonColors {
    return when (variant) {
        PButtonVariant.Primary -> {
            ButtonDefaults.buttonColors(
                containerColor = Purple500,
                contentColor = Color.White
            )
        }

        PButtonVariant.Secondary -> {
            ButtonDefaults.buttonColors(
                containerColor = Gray200,
                contentColor = Gray900
            )
        }
    }
}

@Composable
fun PButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    variant: PButtonVariant = PButtonVariant.Primary,
) {
    PButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        variant = variant,
    ) {
        Text(text, fontSize = 16.sp)
    }
}

@Composable
fun PButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    variant: PButtonVariant = PButtonVariant.Primary,
    content: @Composable () -> Unit
) {
    Button(
        enabled = enabled,
        colors = getVariantColors(variant),
        modifier = modifier
            .height(48.dp),
        shape = RoundedCornerShape(8.dp),
        onClick = onClick,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(
                4.dp,
                alignment = Alignment.CenterHorizontally
            ),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxHeight()
        ) {
            content()
        }
    }
}