package com.example.pamflet.renderer.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.pamflet.FontSize
import com.example.pamflet.parseColor
import pamflet.parser.Element

@Composable
fun PList(el: Element.List) {
    Column(
        horizontalAlignment = Alignment.Start,
    ) {
        el.items.forEach {
            PListItem(it, color = parseColor(el.color), fontSize = FontSize.parse(el.fontSize))
        }
    }
}

@Composable
fun PListItem(item: String, color: Color, fontSize: TextUnit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text("\u2022", color = color, fontSize = fontSize)
        Text(item, color = color, fontSize = fontSize)
    }
}

@Preview(
    showBackground = true
)
@Composable
fun PListPreview() {
    val testPList = Element.List(items = mutableListOf("Aeroplane"))
    PList(testPList)
}