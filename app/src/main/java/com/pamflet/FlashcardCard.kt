package com.pamflet

import androidx.compose.runtime.Composable
import com.pamflet.renderer.Renderer

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