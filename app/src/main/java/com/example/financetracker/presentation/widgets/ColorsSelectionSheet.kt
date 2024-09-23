package com.example.financetracker.presentation.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.financetracker.domain.model.local.StandardColor
import com.example.financetracker.domain.model.local.StandardColor.Companion.toColor
import com.example.financetracker.presentation.ui.theme.FinanceTrackerTheme

@Composable
fun ColorsSelectionSheet(
    modifier: Modifier = Modifier,
    availableColors: List<StandardColor>,
    selectedColor: StandardColor,
    onSelectColor: (StandardColor) -> Unit,

    ) {
    Surface {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            Text(text = "Select Color :", modifier = Modifier.padding(vertical = 12.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(availableColors.size) { index ->
                    val color = availableColors[index]
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clickable {
                                onSelectColor(color)
                            }
                            .background(color = color.toColor(), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (color.hex == selectedColor.hex)
                            Icon(
                                Icons.Default.Check,
                                contentDescription = "Selected Color Icon",
                                tint = Color.White,
                            )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    FinanceTrackerTheme(darkTheme = false) {
        ColorsSelectionSheet(
            availableColors = arrayListOf(
                StandardColor("#0000FF", ""),
                StandardColor("#FFEE23", ""),
                StandardColor("#FF00FF", ""),
                StandardColor("#FFE0FF", ""),
                StandardColor("#FF9245", ""),
            ),
            onSelectColor = {},
            selectedColor = StandardColor("#FF9245", ""),
        )
    }
}

