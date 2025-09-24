package com.example.pamflet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pamflet.parser.Element
import pamflet.parser.Parser

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            App()
        }
    }
}


@Composable
fun App() {
    val content = remember {mutableStateOf("hello world")}
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
        ) {


            Column(
                modifier = Modifier
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val selectedMS = remember { mutableIntStateOf(0) }
                val options = listOf("Code", "Preview")
                SingleChoiceSegmentedButtonRow() {
                    options.forEachIndexed { index, label ->
                        SegmentedButton(
                            selected = selectedMS.intValue == index,
                            label = { Text(label) },
                            onClick = { selectedMS.intValue = index },
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = options.size
                            )
                        )
                    }
                }

                Box() {
                    when (selectedMS.intValue) {
                        0 -> Editor(content)
                        1 -> Flashcard(content)
                        else -> throw Exception("")
                    }
                }

            }


        }
    }
}

@Composable
fun Editor(content: MutableState<String>) {
    Box(
        modifier = Modifier
            .wrapContentSize()
            .padding(16.dp), contentAlignment = Alignment.Center
    ) {
        OutlinedCard(
            border = BorderStroke(width = 1.dp, color = Color.Gray),
            modifier = Modifier
                .sizeIn(maxWidth = 350.dp, maxHeight = 400.dp)
                .fillMaxSize()
        ) {

            BasicTextField(
                modifier = Modifier
                    .fillMaxSize()
                    .border(width = 0.dp, color = Color.Transparent)
                    .padding(16.dp),
                value = content.value,
                onValueChange = { newValue ->
                    content.value = newValue
                }
            )
        }
    }
}

@Composable
fun Flashcard(content: MutableState<String>) {
    Box(
        modifier = Modifier
            .wrapContentSize()
            .padding(16.dp), contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .widthIn(max = 350.dp)
                .heightIn(max = 400.dp)
                .fillMaxSize()
        ) {
            Renderer(content.value)
        }
    }
}


@Composable
fun PText(el: Element.Text) {
    Text(text = el.content)
}

@Composable
fun Renderer(inputchars: String) {
    // not used for now
    val elements = Parser(inputchars).parse()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        elements.forEach { element ->
            when (element) {
                is Element.Text -> PText(element)
                else -> { }
            }
        }
    }
}


@Preview(
    showSystemUi = true,
    showBackground = true,

    )
@Composable
fun PamfletPreview() {
    App()
}