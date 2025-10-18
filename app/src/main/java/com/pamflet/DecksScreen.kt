package com.pamflet

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.pamflet.NavDestination
import com.pamflet.cardStackItems
import com.pamflet.decks
import com.pamflet.ui.theme.Purple100
import com.pamflet.ui.theme.Purple500
import com.pamflet.ui.theme.Purple700
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DecksScreen(
    data: NavDestination.Decks,
    onNavigateToEditorPreviewTest: () -> Unit,
    onNavigateToCardsSlideScreen: (deckIds: List<String>) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.height(86.dp),
                title = {
                    Box(modifier = Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
                        Text("Decks", color = Purple700, fontSize = 18.sp)
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
        Box(modifier = Modifier.padding(paddingValues)) {
            LazyColumn(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                item {
                    Box(
                        modifier = Modifier.padding(
                            start = 16.dp,
                            bottom = 0.dp,
                            end = 16.dp,
                            top = 16.dp
                        )
                    ) {
                        Button(
                            onClick = onNavigateToEditorPreviewTest,
                            colors = ButtonDefaults.buttonColors(containerColor = Purple500),
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.plus),
                                    contentDescription = ""
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("add deck")
                            }
                        }
                    }

                }

                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        decks.forEach { deck ->
                            Card(
                                modifier = Modifier
                                    .sizeIn(
                                        minWidth = 150.dp,
                                        maxWidth = 310.dp,
                                        minHeight = 200.dp,
                                        maxHeight = 350.dp
                                    ),
                                onClick = {
                                    onNavigateToCardsSlideScreen(listOf(deck.id))
                                }
                            ) {
                                Column(
                                    verticalArrangement = Arrangement.SpaceBetween,
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(16.dp)
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .height(250.dp)
                                            .width(200.dp)
                                    ) {
                                        cardStackItems.forEachIndexed { index, cardStackItem ->
                                            Card(
                                                colors = CardDefaults.cardColors(containerColor = cardStackItem.backgroundColor),
                                                modifier = Modifier
                                                    .size(150.dp, 200.dp)
                                                    .background(
                                                        color = Color.Transparent, // not applied because of the colors property
                                                        shape = RoundedCornerShape(12.dp)
                                                    )
                                                    .offset {
                                                        IntOffset(
                                                            x = cardStackItem.offset.x.roundToInt(),
                                                            y = cardStackItem.offset.y.roundToInt()
                                                        )
                                                    }
                                                    .graphicsLayer(
                                                        rotationZ = cardStackItem.rotation,
                                                        transformOrigin = TransformOrigin(
                                                            pivotFractionX = 0.5f,
                                                            pivotFractionY = 0.5f
                                                        )
                                                    )
                                                    .zIndex(index.toFloat()),
                                            ) {}
                                        }
                                    }
                                    Spacer(
                                        modifier = Modifier
                                            .height(4.dp)
                                            .fillMaxWidth()
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = deck.name,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Medium,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.weight(1F)
                                        )
                                        Text(text = deck.cards.size.toString(), fontSize = 14.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}