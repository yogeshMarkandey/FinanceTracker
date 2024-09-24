package com.example.financetracker.data.models.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [Index(value = ["budgetId"]), Index(value = ["transactionId"])]
)
data class BudgetTransactionCrossRef(
    @PrimaryKey val id: String,
    val transactionId: Int,
    val budgetId: Int,
    val status: String
) {
    fun copyWith(status: String? = null): BudgetTransactionCrossRef {
        return BudgetTransactionCrossRef(
            id = id,
            transactionId = this.transactionId,
            budgetId = this.transactionId,
            status = status ?: this.status
        )
    }
}