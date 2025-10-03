package com.example.pamflet

import androidx.compose.runtime.Composable
import com.example.pamflet.renderer.Renderer

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