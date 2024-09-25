package com.example.financetracker.presentation.widgets.category

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.financetracker.domain.model.CategoryAnalysisResult
import java.util.Locale


@Composable
fun CategoryAnalysisContent(catAn: CategoryAnalysisResult) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .background(
                color = Color.Gray.copy(alpha = .25f),
                shape = RoundedCornerShape(6.dp)
            )
            .padding(horizontal = 12.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CategoryIconCompose(
                category = catAn.category,
                onClick = { /* no-op */ },
                isSelected = false,
                modifier = Modifier.width(80.dp)
            )
            Box(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = catAn.category.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Box(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                color = Color.Cyan,
                                shape = RoundedCornerShape(6.dp)
                            )
                            .height(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(catAn.getPercentageConsumed() / 100f)
                                .background(
                                    color = Color.Red,
                                    shape = RoundedCornerShape(6.dp)
                                )
                                .height(12.dp)
                        )
                    }

                    Text(
                        text = String.format(
                            Locale.ENGLISH,
                            "%.1f",
                            catAn.getPercentageConsumed()
                        ) + "%",
                        modifier = Modifier.padding(
                            horizontal = 12.dp,
                            vertical = 6.dp
                        ),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Box(modifier = Modifier.height(6.dp))
                Text(
                    text = "Consumed " + String.format(
                        Locale.ENGLISH,
                        "%.1f",
                        catAn.getPercentageConsumed()
                    ) + "% of the category budget",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}