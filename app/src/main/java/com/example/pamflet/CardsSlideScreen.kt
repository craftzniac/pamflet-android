package com.example.pamflet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pamflet.ui.theme.Purple100

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardsSlideScreen(
    cardsSlide: NavDestination.CardsSlide,
    onNavigateBack: () -> Unit
) {
    val deckIds = cardsSlide.deckIds
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.height(86.dp),
                title = {
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        val decknames = listOf(
                            "geography",
                            "fundamentals of programming in C",
                            "riscv assembly: memory unit"
                        )
                        Text(
                            "Card Slide",
                            color = Color.Black,
                            fontSize = 18.sp,
                            lineHeight = 10.sp
                        )
                        Text(
                            text = decknames.joinToString(separator = ", "),
                            color = Color(0xFF555555),
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            lineHeight = 12.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_left),
                            contentDescription = "arrow back icon"
                        )
                    }
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .size(45.dp)
                            .background(
                                shape = RoundedCornerShape(percent = 100),
                                color = Color.LightGray
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "J", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Purple100),
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues).fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            val deck = decks[0]
            val selectedCardMutState = remember { mutableStateOf(deck.cards.get(0)) }
            val isFlippedMutState = remember { mutableStateOf(false) }

            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Column {
                    FlashcardCard(
                        card = selectedCardMutState.value,
                        isFlipped = isFlippedMutState.value
                    )
                    Button(
                        modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                        onClick = { isFlippedMutState.value = !isFlippedMutState.value }) {
                        Text("flip")
                    }
                }
                Spacer(
                    modifier = Modifier
                        .height(8.dp)
                        .fillMaxWidth()
                )
                CardSwipeButtons()
            }

        }
    }
}


@Composable
fun CardSwipeButtons() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .widthIn(max = 200.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = {}) {
            Icon(
                painter = painterResource(R.drawable.arrow_left),
                contentDescription = "arrow left icon"
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(onClick = {}) {
            Icon(
                painter = painterResource(R.drawable.arrow_right),
                contentDescription = "arrow left icon"
            )
        }
    }
}