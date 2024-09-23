package com.example.financetracker.presentation.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.financetracker.presentation.ui.theme.FinanceTrackerTheme

@Composable
fun IconSelectionSheet(
    modifier: Modifier = Modifier,
    availableIcons: List<ImageVector>,
    selectedIcon: ImageVector,
    onSelectIcon: (ImageVector) -> Unit,
    selectedColor: Color,
) {
    val scrollState = rememberScrollState()
    Surface {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .scrollable(scrollState, Orientation.Vertical)
                .padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            Text(text = "Select Icon :", modifier = Modifier.padding(vertical = 12.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(availableIcons.size) { index ->
                    val vector = availableIcons[index]
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clickable {
                                onSelectIcon(vector)
                            }
                            .background(
                                color =
                                if (vector.name == selectedIcon.name)
                                    Color.Gray.copy(0.4f)
                                else Color.Gray.copy(alpha = 0.1f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            vector,
                            contentDescription = "Selected Color Icon",
                            tint = selectedColor,
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
        IconSelectionSheet(
            availableIcons = arrayListOf(Icons.Default.Edit, Icons.Default.Info),
            selectedIcon = Icons.Default.Info,
            onSelectIcon = {},
            selectedColor = Color.Cyan
        )
    }
}

