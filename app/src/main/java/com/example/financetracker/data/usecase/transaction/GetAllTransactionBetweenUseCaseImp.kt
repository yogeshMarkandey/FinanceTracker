package com.example.financetracker.data.usecase.transaction

import com.example.financetracker.domain.model.local.Transaction
import com.example.financetracker.domain.model.repository.TransactionRepository
import com.example.financetracker.domain.model.usecase.transaction.GetAllTransactionBetweenUseCase
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject

class GetAllTransactionBetweenUseCaseImp @Inject constructor(
    private val transactionRepository: TransactionRepository
) : GetAllTransactionBetweenUseCase {
    override fun execute(start: Date, end: Date): Flow<List<Transaction>> {
       return transactionRepository.getAllTransaction()
    }
}