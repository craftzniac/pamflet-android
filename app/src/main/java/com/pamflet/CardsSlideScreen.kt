package com.pamflet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import com.pamflet.FlashcardCard
import com.pamflet.NavDestination
import com.pamflet.decks
import com.pamflet.ui.theme.Purple100

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardsSlideScreen(
    cardsSlide: NavDestination.CardsSlide,
    onNavigateBack: () -> Unit
) {
    val deckIds = cardsSlide.deckIds
    val decknames = decks.map { it.name }
    val cards = decks[0].cards
    val selectedCardMutState = remember { mutableStateOf(cards[0]) }
    val isFlippedMutState = remember { mutableStateOf(false) }
    val pagerState = rememberPagerState(pageCount = { cards.size })

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.height(86.dp),
                title = {
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.Center
                    ) {
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
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    HorizontalPager(
                        state = pagerState,
                        pageSpacing = 8.dp,
                        contentPadding = PaddingValues(horizontal = 20.dp)
                    ) { page ->
                        FlashcardCard(
                            card = cards[page],
                            isFlipped = isFlippedMutState.value
                        )
                    }
                    Button(
                        modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                        onClick = { isFlippedMutState.value = !isFlippedMutState.value }) {
                        Text("flip")
                    }
                }
            }

        }
    }
}