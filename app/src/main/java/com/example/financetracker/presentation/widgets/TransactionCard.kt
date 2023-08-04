package com.example.financetracker.presentation.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.financetracker.data.models.local.TransactionModel

@Composable
fun TransactionCard(
    modifier: Modifier = Modifier,
    data: TransactionModel
){
    Card(modifier) {
        Column(modifier= Modifier.fillMaxWidth().padding(all= 8.dp)) {
            Text(text = data.date)
            Text(text = data.valueDate)
            Text(text = data.description)
            Text(text = data.amount.toString())
            Text(text = data.transactionType.toString())
            Text(text = data.balance.toString())
        }
    }
}