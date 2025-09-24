package com.example.pamflet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TextApp() {
    Scaffold { paddingValues ->
        Column (modifier = Modifier.padding(paddingValues)){
            val text = "hello mike"
            TextComp(
                text,
                24.sp,
                Color.Red,
                FontWeight.Normal,
                fontStyleValue = FontStyle.Italic,
            )
        }
    }
}


@Composable
fun TextComp(
    value: String,
    size: TextUnit = 18.sp,
    colorValue: Color = Color.Green,
    fontWeightValue: FontWeight = FontWeight.Normal,
    fontStyleValue: FontStyle = FontStyle.Normal,
    maxLinesValue: Int? = null,
    backgroundColor: Color = Color.White
) {
    Text(
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight(align = Alignment.Bottom)
            //.fillMaxSize()
            .clip(shape = RoundedCornerShape(8.dp))
            .border(width = 2.dp, color = Color.Magenta)
            .background(backgroundColor)
            .padding(18.dp)
        // .alpha(0.5f),  // opacity alpha
        ,
        text = value,
        fontSize = size,
        color = colorValue,
        fontWeight = fontWeightValue,
        fontStyle = fontStyleValue,
        maxLines = maxLinesValue ?: Int.MAX_VALUE,
        overflow = TextOverflow.Ellipsis
    )
}

