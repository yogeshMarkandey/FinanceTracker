package com.example.financetracker.presentation.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.financetracker.domain.model.local.Budget
import com.example.financetracker.presentation.ui.theme.FinanceTrackerTheme

@Composable
fun BudgetSelectionSheetContent(
    modifier: Modifier = Modifier,
    onBudgetSelected: (List<Budget>) -> Unit,
    availableBudgets: List<Budget>,
    prevSelectedBudgets: List<Budget>,
) {
    val selectedBudget = remember { mutableStateListOf<Budget>() }

    LaunchedEffect(key1 = Unit) {
        selectedBudget.addAll(prevSelectedBudgets)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 12.dp)
    ) {

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Select Budget", fontWeight = FontWeight.Bold, fontSize = 16.sp)

                IconButton(onClick = { onBudgetSelected(selectedBudget.toList()) }) {
                    Icon(Icons.Default.Check, contentDescription = "Save Selection Icon")
                }
            }
            Box(modifier = Modifier.height(20.dp))
        }

        items(availableBudgets.size) { index ->
            val budget = availableBudgets[index]

            var selected = false
            selectedBudget.forEach {
                if (it.id == budget.id) {
                    selected = true
                }
            }

            Row(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxWidth()
                    .background(
                        color = Color.Gray.copy(alpha = 0.25f),
                        shape = RoundedCornerShape(6.dp)
                    )
                    .clickable {
                        if (selected) {
                            selectedBudget.remove(budget)
                        } else {
                            selectedBudget.add(budget)
                        }
                    }
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = budget.title, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    Box(modifier = Modifier.height(6.dp))
                    Text(
                        text = "${budget.getReadableStartDate()}-${budget.getReadableEndDate()}",
                        fontWeight = FontWeight.Light,
                        fontSize = 12.sp
                    )
                }
                AnimatedVisibility(visible = selected, enter = fadeIn(), exit = fadeOut()) {
                    Icon(Icons.Default.CheckCircle, contentDescription = "Selected Icon")
                }
            }
        }


        item {
            Box(modifier = Modifier.height(20.dp))
        }

    }
}

@Preview
@Composable
private fun Preview() {
    FinanceTrackerTheme {
        val selectedBudget = Budget.getDefault()
        Surface {
            BudgetSelectionSheetContent(
                availableBudgets = listOf(Budget.getDefault(), selectedBudget),
                onBudgetSelected = {},
                prevSelectedBudgets = listOf(selectedBudget)
            )
        }
    }
}

