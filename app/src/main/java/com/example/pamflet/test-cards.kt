package com.example.pamflet

import pamflet.parser.generateId
import pamflet.testStrings

data class Flashcard(
    val id: String,
    var front: String,
    var back: String,
)

val cards = listOf(
    Flashcard(id = generateId(), front = testStrings[0], back = testStrings[1]),
    Flashcard(id = generateId(), front = testStrings[2], back = testStrings[3]),
    Flashcard(id = generateId(), front = testStrings[4], back = testStrings[5]),
    Flashcard(id = generateId(), front = testStrings[6], back = testStrings[7]),
    Flashcard(id = generateId(), front = testStrings[8], back = ""),
)
