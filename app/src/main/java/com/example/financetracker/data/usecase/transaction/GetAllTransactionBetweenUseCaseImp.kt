package com.example.financetracker.data.usecase.transaction

import com.example.financetracker.domain.model.local.Transaction
import com.example.financetracker.domain.repository.TransactionRepository
import com.example.financetracker.domain.usecase.transaction.GetAllTransactionBetweenUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.util.Date
import javax.inject.Inject

class GetAllTransactionBetweenUseCaseImp @Inject constructor(
    private val transactionRepository: TransactionRepository
) : GetAllTransactionBetweenUseCase {
    override fun execute(start: Date, end: Date): Flow<List<Transaction>> {
       return transactionRepository.getAllTransaction()
    }

    override suspend fun executeAsList(start: Date, end: Date): List<Transaction> {
        return execute(start, end).first()
    }
}