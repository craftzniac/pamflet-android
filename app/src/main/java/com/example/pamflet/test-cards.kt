package com.example.pamflet

import pamflet.parser.generateId
import pamflet.testStrings

data class Flashcard(
    val id: String,
    var front: String,
    var back: String,
)

val cards = listOf(
    Flashcard(id = generateId(), front = testStrings[0], back = testStrings[1]),    // 0
    Flashcard(id = generateId(), front = testStrings[2], back = testStrings[3]),    // 1
    Flashcard(id = generateId(), front = testStrings[4], back = testStrings[5]),    // 2
    Flashcard(id = generateId(), front = testStrings[6], back = testStrings[7]),    // 3
    Flashcard(id = generateId(), front = testStrings[0], back = testStrings[8]),    // 4
    Flashcard(
        id = generateId(),
        front = "Name 3 planets in our solar system\n.color: steelblue\n.fontSize: 2xl",
        back = "- Mars\n- Jupiter\n- Venus\n.color: rebeccapurple\n.fontSize: xl"
    ),                                                                              // 5
    Flashcard(
        id = generateId(),
        front = "Which is an African country?\n- ghana\n- canada\n- malaysia\n- singapore\n.correct: 0",
        back = ""
    ),                                                                              // 6
    Flashcard(
        id = generateId(),
        front = "Which of these are African country?\n- ghana\n- botswana\n- malaysia\n- singapore\n.correct: 0, 1",
        back = ""                                                                   // 7
    )
)
