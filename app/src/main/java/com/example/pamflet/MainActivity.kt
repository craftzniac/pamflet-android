package com.example.pamflet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

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
    val selectedCardMutState =  remember { mutableStateOf(cards[1]) }

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val selectedMS = remember { mutableIntStateOf(0) }
                val options = listOf(
                    stringResource(R.string.editor_code),
                    stringResource(R.string.editor_preview)
                )
                var isFlipped by remember {mutableStateOf(false)}
                SingleChoiceSegmentedButtonRow {
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

                Box {
                    when (selectedMS.intValue) {
                        0 -> EditorCard(selectedCardMutState, isFlipped)
                        1 -> FlashcardCard(selectedCardMutState.value, isFlipped)
                        else -> throw Exception("")
                    }
                }

                Button (onClick = { isFlipped = !isFlipped }){
                    Text("Flip")
                }
            }
        }
    }
}


@Preview( showSystemUi = true, showBackground = true)
@Composable
fun PamfletPreview() {
    App()
}