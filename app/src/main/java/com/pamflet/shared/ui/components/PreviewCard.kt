package com.pamflet.shared.ui.components

import androidx.compose.runtime.Composable
import com.pamflet.core.domain.Flashcard
import com.pamflet.shared.ui.renderer.Renderer

@Composable
fun PreviewCard(
    cardFrontContent: String,
    cardBackContent: String,
    isFlipped: Boolean) {
    FlippableCard(
        isFlipped = isFlipped,
        front = {
            Renderer(content = cardFrontContent, cardFace = CardFace.Front)
        },
        back = { modifier ->
            Renderer(modifier, content = cardBackContent, cardFace = CardFace.Back)
        }
    )
}