package com.example.financetracker.presentation.widgets.transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TransactionSummaryContent(
    modifier: Modifier = Modifier,
    spendingAmount: Float,
    incomeAmount: Float,
    balanceAmount: Float,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "This month",
            modifier = Modifier
                .align(Alignment.Start)
                .padding(horizontal = 12.dp, vertical = 12.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        ) {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .weight(1f)
                    .background(
                        color = Color.Green.copy(alpha = .25f),
                        shape = CircleShape
                    )
                    .padding(vertical = 12.dp, horizontal = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    Text(
                        text = "$$incomeAmount",
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 20.sp
                    )
                    Text(
                        text = "Income",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 14.sp
                    )
                }
            }
            Box(modifier = Modifier.width(12.dp))
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .weight(1f)
                    .background(
                        color = Color.Red.copy(alpha = .25f),
                        shape = CircleShape
                    )
                    .padding(vertical = 12.dp, horizontal = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    Text(
                        text = "$$spendingAmount",
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 20.sp
                    )
                    Text(
                        text = "Expense",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 14.sp
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .padding(vertical = 4.dp)
                .background(
                    color = Color.Black.copy(alpha = .2f),
                    shape = CircleShape
                )
                .padding(vertical = 8.dp, horizontal = 25.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(modifier = Modifier.wrapContentSize()) {
                Text(
                    text = "$$balanceAmount",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp
                )
                Text(
                    text = "Balance",
                    textAlign = TextAlign.Center,
                    fontSize = 10.sp
                )
            }
        }

    }
}