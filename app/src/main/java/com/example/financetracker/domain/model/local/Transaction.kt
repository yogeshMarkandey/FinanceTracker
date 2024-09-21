package com.example.financetracker.domain.model.local

import com.example.financetracker.data.models.TransactionEntity
import com.example.financetracker.data.models.local.PaymentType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Transaction(
    val amount: Float,
    val id: Int,
    val dateTime: Date,
    val categoryId: Int,
    val tagId: Int,
    val notes: String,
    val paymentType: PaymentType,
    val paymentSourceId: Int,
    var category: Category = Category.newInstance()
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

        fun defaultList() = listOf(
            Transaction(
                id = 34,
                amount = 334.4f,
                dateTime = Date(),
                categoryId = 39,
                tagId = 0,
                notes = "Bike",
                paymentType = PaymentType.Expense,
                paymentSourceId = 0,
                category = Category.other()
            ),
            Transaction(
                id = 36,
                amount = 100.4f,
                dateTime = Date(),
                categoryId = 39,
                tagId = 0,
                notes = "Car wipenpwe eiwpepeoc iepw  onwoenwpeon eiweinwe eowp",
                paymentType = PaymentType.Expense,
                paymentSourceId = 0,
                category = Category.other()
            )
        )
    }

    fun getReadableDateTime(): String {
        val dateFormat = SimpleDateFormat("hh:mm a, dd/MMM", Locale.ENGLISH)
        return dateFormat.format(dateTime)
    }
}