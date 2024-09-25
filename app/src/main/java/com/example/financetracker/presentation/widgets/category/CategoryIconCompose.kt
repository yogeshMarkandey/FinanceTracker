package com.example.financetracker.presentation.widgets.category

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.financetracker.domain.model.local.Category
import com.example.financetracker.domain.model.local.StandardColor.Companion.toColor
import com.example.financetracker.presentation.common.IconHelper

@Composable
fun CategoryIconCompose(
    modifier: Modifier = Modifier,
    category: Category,
    onClick: () -> Unit,
    isSelected: Boolean
) {
    Box(
        modifier = modifier
            .clickable {
                onClick()
            }
            .aspectRatio(1f)
            .background(
                color = if (isSelected) Color.Gray.copy(0.40f) else Color.Gray.copy(0.10f),
                shape = CircleShape
            )
            .padding(6.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    IconHelper.getIconByName(category.icon),
                    contentDescription = "Category Icon",
                    tint = category.color.toColor(),
                    modifier = Modifier.size(36.dp)
                )
                Box(modifier = Modifier.height(2.dp))
                Text(
                    text = category.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = category.color.toColor(),
                    maxLines = 1,
                    fontWeight = FontWeight.Light,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    Surface(modifier = Modifier.width(100.dp)) {
        CategoryIconCompose(category = Category.getDefaults()[1], isSelected = false, onClick = {})
    }
}