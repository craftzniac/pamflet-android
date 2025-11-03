package com.pamflet.features.deck.card.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.pamflet.R
import com.pamflet.core.domain.Flashcard
import com.pamflet.shared.ui.components.EditorCard
import com.pamflet.shared.ui.components.FlashcardFlipButton
import com.pamflet.shared.ui.components.PreviewCard

enum class CardSelectionMode {
    Edit,
    Preview
}

@Composable
fun EditAndPreviewCard(
    card: Flashcard,
    updateCardFront: (value: String) -> Unit,
    updateCardBack: (value: String) -> Unit,
) {
    val isFlippedMutState = remember { mutableStateOf(false) }
    val cardSelectionModeMutState = remember { mutableStateOf(CardSelectionMode.Preview) }

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .widthIn(max = 300.dp)
                .fillMaxWidth()
        ) {
            SingleChoiceSegmentedButtonRow {
                SegmentedButton(  // edit button
                    selected = cardSelectionModeMutState.value == CardSelectionMode.Edit,
                    onClick = {
                        cardSelectionModeMutState.value = CardSelectionMode.Edit
                    },
                    shape = SegmentedButtonDefaults.itemShape(
                        index = 0,
                        count = CardSelectionMode.entries.size
                    ),
                    icon = {},
                    label = {
                        Icon(
                            painter = painterResource(R.drawable.text_block),
                            contentDescription = "text block",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                )
                SegmentedButton(  // edit button
                    selected = cardSelectionModeMutState.value == CardSelectionMode.Preview,
                    onClick = {
                        cardSelectionModeMutState.value =
                            CardSelectionMode.Preview
                    },
                    shape = SegmentedButtonDefaults.itemShape(
                        index = 1,
                        count = CardSelectionMode.entries.size
                    ),
                    icon = {},
                    label = {
                        Icon(
                            painter = painterResource(R.drawable.picture),
                            contentDescription = "picture",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            when (cardSelectionModeMutState.value) {
                CardSelectionMode.Edit -> {
                    EditorCard(
                        card = card,
                        updateCardFront = updateCardFront,
                        updateCardBack = updateCardBack,
                        isFlipped = isFlippedMutState.value
                    )
                }

                CardSelectionMode.Preview -> {
                    PreviewCard(
                        card = card,
                        isFlipped = isFlippedMutState.value
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            FlashcardFlipButton(
                onClick = { isFlippedMutState.value = !isFlippedMutState.value }
            )
        }
    }
}
