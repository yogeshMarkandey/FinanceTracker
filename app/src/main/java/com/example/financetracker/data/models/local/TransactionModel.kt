package com.example.financetracker.data.models.local

data class TransactionModel(
    val date: String,
    val valueDate: String,
    val description: String,
    val refNo: String,
    val amount: Float,
    val transactionType: TransactionType,
    val balance: Float
)

enum class TransactionType{
    Credit,
    Debit
}