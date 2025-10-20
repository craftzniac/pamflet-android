package com.pamflet.renderer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pamflet.components.CardFace
import com.pamflet.renderer.components.PLink
import com.pamflet.renderer.components.PList
import com.pamflet.renderer.components.PMultichoice
import com.pamflet.renderer.components.PText
import pamflet.parser.Element
import pamflet.parser.Parser

@Composable
fun Renderer(modifier: Modifier = Modifier, content: String, cardFace: CardFace) {
    val elements = remember { Parser(content).parse() }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        WhichFaceChip(cardFace)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (elements.isEmpty()) {
                item {
                    Text("<Empty>", fontSize = 18.sp, style = TextStyle(fontStyle = FontStyle.Italic), color = Color(0xFFAAAAAA))
                }
            } else {
                items(elements) { element ->
                    when (element) {
                        is Element.Text -> PText(element)
                        is Element.Link -> PLink(element)
                        is Element.List -> PList(element)
                        is Element.Multichoice -> PMultichoice(element)
                        else -> {}
                    }
                }
            }
        }
    }
}

@Composable
fun WhichFaceChip(cardFace: CardFace, backgroundColor: Color? = null, color: Color? = null) {
    Box(
        modifier = Modifier
            .background(color = backgroundColor ?: Color.Gray, shape = RoundedCornerShape(4.dp))
            .padding(4.dp, 2.dp)
    ) {
        Text(text = cardFace.toString().lowercase(), color = color ?: Color.White)
    }
}
