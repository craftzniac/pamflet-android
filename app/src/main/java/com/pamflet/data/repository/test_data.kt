package com.pamflet.data.repository

import com.pamflet.data.local.entity.DeckEntity
import com.pamflet.data.local.entity.FlashcardEntity
import pamflet.testStrings
import java.util.UUID


val flashcardEntities = listOf(
    FlashcardEntity(
        id = UUID.randomUUID().toString(),
        front = testStrings[0],
        back = testStrings[1],
        deckId = ""
    ),    // 0
    FlashcardEntity(
        id = UUID.randomUUID().toString(),
        front = testStrings[2],
        back = testStrings[3],
        deckId = ""
    ),    // 1
    FlashcardEntity(
        id = UUID.randomUUID().toString(),
        front = testStrings[4],
        back = testStrings[5],
        deckId = ""
    ),    // 2
    FlashcardEntity(
        id = UUID.randomUUID().toString(),
        front = testStrings[6],
        back = testStrings[7],
        deckId = ""
    ),    // 3
    FlashcardEntity(
        id = UUID.randomUUID().toString(),
        front = testStrings[0],
        back = testStrings[8],
        deckId = ""
    ),    // 4
    FlashcardEntity(
        id = UUID.randomUUID().toString(),
        front = "Name 3 planets in our solar system\n.color: steelblue\n.fontSize: 2xl",
        back = "- Mars\n- Jupiter\n- Venus\n.color: rebeccapurple\n.fontSize: xl",
        deckId = ""
    ),                                                                              // 5
    FlashcardEntity(
        id = UUID.randomUUID().toString(),
        front = "Which is an African country?\n- ghana\n- canada\n- malaysia\n- singapore\n.correct: 0",
        back = "",
        deckId = ""
    ),                                                                              // 6
    FlashcardEntity(
        id = UUID.randomUUID().toString(),
        front = "Which of these are African country?\n- ghana\n- botswana\n- malaysia\n- singapore\n.correct: 0, 1",
        back = "",
        deckId = "" // 7
    )
)

val deckEntities: List<DeckEntity> = listOf(
    DeckEntity(
        id = UUID.randomUUID().toString(),
        name = "language design fundamentals",
    ),
    DeckEntity(
        id = UUID.randomUUID().toString(),
        name = "Weather reports",
    ),
    DeckEntity(
        id = UUID.randomUUID().toString(),
        name = "C programming",
    ),
    DeckEntity(
        id = UUID.randomUUID().toString(),
        name = "Data structures and Algorithms",
    ),
    DeckEntity(
        id = UUID.randomUUID().toString(),
        name = "Physics: Optics",
    ),
    DeckEntity(
        id = UUID.randomUUID().toString(),
        name = "Aerodynamics II",
    ),
    DeckEntity(
        id = UUID.randomUUID().toString(),
        name = "The Design and Nature of Code",
    ),
    DeckEntity(
        id = UUID.randomUUID().toString(),
        name = "Humane Software",
    ),
    DeckEntity(
        id = UUID.randomUUID().toString(),
        name = "Geography",
    )
)