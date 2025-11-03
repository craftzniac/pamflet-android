package com.pamflet.shared.ui.components

import androidx.compose.runtime.Composable
import com.pamflet.core.domain.Flashcard
import com.pamflet.shared.ui.renderer.Renderer

@Composable
fun PreviewCard(card: Flashcard, isFlipped: Boolean) {
    FlippableCard(
        isFlipped = isFlipped,
        front = {
            Renderer(content = card.front, cardFace = CardFace.Front)
        },
        back = { modifier ->
            Renderer(modifier, content = card.back, cardFace = CardFace.Back)
        }
    )
}