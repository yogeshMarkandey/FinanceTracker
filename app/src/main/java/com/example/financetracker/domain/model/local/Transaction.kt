package com.example.financetracker.domain.model.local

import com.example.financetracker.data.models.TransactionEntity
import com.example.financetracker.data.models.local.PaymentType
import java.util.Date

data class Transaction(
    val amount: Float,
    val id: Int,
    val dateTime: Date,
    val categoryId: Int,
    val tagId: Int,
    val notes: String,
    val paymentType: PaymentType,
    val paymentSourceId: Int
) {
    companion object {
        fun TransactionEntity.toTransaction(): Transaction {
            return Transaction(
                id = this.id,
                amount = this.amount,
                dateTime = this.dateTime,
                categoryId = this.categoryId,
                tagId = this.tagId,
                notes = this.notes,
                paymentType = this.paymentType,
                paymentSourceId = this.paymentSourceId
            )
        }

        fun Transaction.toTransactionEntity(): TransactionEntity {
            return TransactionEntity(
                id = this.id,
                amount = this.amount,
                dateTime = this.dateTime,
                categoryId = this.categoryId,
                tagId = this.tagId,
                notes = this.notes,
                paymentType = this.paymentType,
                paymentSourceId = this.paymentSourceId
            )
        }
    }
}