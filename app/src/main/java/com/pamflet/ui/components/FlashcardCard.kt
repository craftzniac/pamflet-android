package com.pamflet.ui.components

import androidx.compose.runtime.Composable
import com.pamflet.ui.screens.Flashcard
import com.pamflet.ui.renderer.Renderer

@Composable
fun FlashcardCard(card: Flashcard, isFlipped: Boolean) {
   FlippableCard(
      isFlipped = isFlipped,
       front = {
           Renderer(content = card.front, cardFace = CardFace.Front)
       },
       back = { modifier ->
           Renderer(modifier, content =card.back, cardFace = CardFace.Back)
       }
   )
}