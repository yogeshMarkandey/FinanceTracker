package com.example.financetracker.domain.usecase.transaction

import com.example.financetracker.domain.model.local.Transaction
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface GetAllTransactionBetweenUseCase {
    fun execute(start: Date, end: Date): Flow<List<Transaction>>
    suspend fun executeAsList(start: Date, end: Date): List<Transaction>
}