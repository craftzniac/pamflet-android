package com.pamflet.data.repository

import com.pamflet.data.local.entity.DeckEntity
import com.pamflet.data.local.entity.FlashcardEntity
import pamflet.parser.generateId
import pamflet.testStrings


val flashcardEntities = listOf(
    FlashcardEntity(
        id = generateId(),
        front = testStrings[0],
        back = testStrings[1],
        deckId = ""
    ),    // 0
    FlashcardEntity(
        id = generateId(),
        front = testStrings[2],
        back = testStrings[3],
        deckId = ""
    ),    // 1
    FlashcardEntity(
        id = generateId(),
        front = testStrings[4],
        back = testStrings[5],
        deckId = ""
    ),    // 2
    FlashcardEntity(
        id = generateId(),
        front = testStrings[6],
        back = testStrings[7],
        deckId = ""
    ),    // 3
    FlashcardEntity(
        id = generateId(),
        front = testStrings[0],
        back = testStrings[8],
        deckId = ""
    ),    // 4
    FlashcardEntity(
        id = generateId(),
        front = "Name 3 planets in our solar system\n.color: steelblue\n.fontSize: 2xl",
        back = "- Mars\n- Jupiter\n- Venus\n.color: rebeccapurple\n.fontSize: xl",
        deckId = ""
    ),                                                                              // 5
    FlashcardEntity(
        id = generateId(),
        front = "Which is an African country?\n- ghana\n- canada\n- malaysia\n- singapore\n.correct: 0",
        back = "",
        deckId = ""
    ),                                                                              // 6
    FlashcardEntity(
        id = generateId(),
        front = "Which of these are African country?\n- ghana\n- botswana\n- malaysia\n- singapore\n.correct: 0, 1",
        back = "",
        deckId = "" // 7
    )
)

val deckEntities: List<DeckEntity> = listOf(
    DeckEntity(
        id = generateId(),
        name = "language design fundamentals",
    ),
    DeckEntity(
        id = generateId(),
        name = "Weather reports",
    ),
    DeckEntity(
        id = generateId(),
        name = "C programming",
    ),
    DeckEntity(
        id = generateId(),
        name = "Data structures and Algorithms",
    ),
    DeckEntity(
        id = generateId(),
        name = "Physics: Optics",
    ),
    DeckEntity(
        id = generateId(),
        name = "Aerodynamics II",
    ),
    DeckEntity(
        id = generateId(),
        name = "The Design and Nature of Code",
    ),
    DeckEntity(
        id = generateId(),
        name = "Humane Software",
    ),
    DeckEntity(
        id = generateId(),
        name = "Geography",
    )
)