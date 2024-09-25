package com.example.financetracker.presentation.widgets.budget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.financetracker.domain.model.local.Budget

@Composable
fun ActiveBudgetItemContent(modifier: Modifier, budget: Budget, onBudgetClicked: () -> Unit) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color.LightGray.copy(alpha = 0.25f),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onBudgetClicked() }
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(Icons.Default.List, contentDescription = "Budget Icon")
        Box(modifier = Modifier.width(12.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = budget.title,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
            Box(modifier = Modifier.height(6.dp))
            Text(
                text = "${budget.getReadableStartDate()} - ${budget.getReadableEndDate()}",
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Light,
                fontSize = 12.sp
            )
        }
        Box(modifier = Modifier.width(12.dp))
        Icon(Icons.Default.ArrowForward, contentDescription = "Open Icon")
        Box(modifier = Modifier.width(12.dp))
    }
}