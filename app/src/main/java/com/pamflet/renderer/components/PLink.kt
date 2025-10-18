package com.pamflet.renderer.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import com.pamflet.FontSize
import com.pamflet.parseColor
import pamflet.parser.Element

@Composable
fun PLink(el: Element.Link) {
    Text(
        text = buildAnnotatedString {
            withLink(
                LinkAnnotation.Url(
                    url = el.href,
                    styles = TextLinkStyles(
                        style = SpanStyle(
                            color = parseColor(el.color, default = Color.Blue),
                            textDecoration = TextDecoration.Underline,
                            fontSize = FontSize.parse(el.fontSize, default = FontSize.Base)
                        )
                    )
                )
            ) {
                append(el.linkText)
            }
        }
    )
}
