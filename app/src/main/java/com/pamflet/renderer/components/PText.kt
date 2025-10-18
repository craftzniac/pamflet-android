package com.pamflet.renderer.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.pamflet.FontSize
import com.pamflet.pamfletTextAlignToJetpackComposeTextAlign
import com.pamflet.parseColor
import pamflet.parser.Element

@Composable
fun PText(el: Element.Text) {
    val color = parseColor(el.color, Color.Black)

    Text(
        modifier = Modifier.fillMaxWidth(),
        text = el.content,
        color = color,
        fontSize = FontSize.parse(el.fontSize, default = FontSize.Base),
        textAlign = pamfletTextAlignToJetpackComposeTextAlign(el.textAlign)
    )
}