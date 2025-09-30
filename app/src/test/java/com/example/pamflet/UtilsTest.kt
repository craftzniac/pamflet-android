package com.example.pamflet

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import org.junit.Test
import org.junit.Assert.*

class UtilsTest {
    @Test
    fun testParseColorWithNamedCSSColor(){
        val green = Color(0xFF008000)
        assertEquals(green, parseColor("green"))
    }


}