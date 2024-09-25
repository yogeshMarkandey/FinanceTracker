package com.example.financetracker.presentation.widgets.transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.financetracker.data.models.local.PaymentType
import com.example.financetracker.domain.model.local.Transaction
import com.example.financetracker.presentation.ui.theme.FinanceTrackerTheme
import com.example.financetracker.presentation.widgets.category.CategoryIconCompose

@Composable
fun TransactionDetailsWidget(
    modifier: Modifier = Modifier,
    transaction: Transaction,
    onTransactionClicked: () -> Unit = {},
) {
    TransactionDetailsContent(
        modifier = modifier,
        transaction = transaction,
        onTransactionClicked = onTransactionClicked,
    )
}

@Composable
private fun TransactionDetailsContent(
    modifier: Modifier = Modifier,
    transaction: Transaction,
    onTransactionClicked: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .fillMaxWidth()
            .background(
                color = Color.LightGray.copy(alpha = 0.25f),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onTransactionClicked() }
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CategoryIconCompose(
            modifier = Modifier.width(80.dp),
            category = transaction.category,
            isSelected = false,
            onClick = {},
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Rs ${transaction.amount} /-",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = transaction.getReadableDateTime(),
                    fontWeight = FontWeight.Light,
                    fontSize = 12.sp
                )
            }
            Box(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = transaction.notes,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    maxLines = 2,
                    modifier = Modifier.weight(1f)
                )
                Box(modifier = Modifier.width(12.dp))

                val color =
                    if (transaction.paymentType == PaymentType.Expense)
                        Color.Red.copy(alpha = 0.25f)
                    else Color.Green.copy(alpha = 0.25f)

                Box(
                    modifier = Modifier
                        .padding(horizontal = 2.dp, vertical = 2.dp)
                        .background(
                            color = color,
                            shape = RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = transaction.paymentType.name,
                        fontWeight = FontWeight.Light,
                        fontSize = 12.sp,
                        maxLines = 1,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    FinanceTrackerTheme {
        Surface {
            TransactionDetailsContent(
                modifier = Modifier,
                transaction = Transaction.defaultList()[1]
            )
        }
    }
}