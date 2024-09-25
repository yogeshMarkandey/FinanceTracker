package com.example.financetracker.domain.repository

import com.example.financetracker.data.models.local.TransactionModel
import com.example.financetracker.domain.model.local.Category
import com.example.financetracker.domain.model.local.Transaction
import kotlinx.coroutines.flow.Flow
import java.io.InputStream
import java.util.Date

interface TransactionRepository {

    fun getTransactionFromExcelFile(inputStream: InputStream): List<TransactionModel>

    fun getAllTransaction() : Flow<List<Transaction>>
    fun getAllBetween(start: Date, end: Date) : Flow<List<Transaction>>
    fun updateTransaction(transaction: Transaction)
    fun addTransaction(transaction: Transaction)
    fun getTransactionById(id: Int): Transaction?

    fun getAllCategories(): Flow<List<Category>>
    fun updateCategory(category: Category)
    fun addCategory(category: Category)
    fun getCategoryById(categoryId: Int) : Category
}