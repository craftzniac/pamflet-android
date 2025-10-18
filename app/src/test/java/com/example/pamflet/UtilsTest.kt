package com.example.pamflet

import androidx.compose.ui.graphics.Color
import org.junit.Test
import org.junit.Assert.*

class UtilsTest {
    @Test
    fun testParseColorWithNamedCSSColor(){
        val green = Color(0xFF008000)
        assertEquals(green, parseColor("green"))
    }


}