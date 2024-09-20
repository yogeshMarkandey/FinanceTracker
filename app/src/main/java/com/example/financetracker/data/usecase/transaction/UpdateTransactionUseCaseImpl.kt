package com.example.financetracker.data.usecase.transaction

import com.example.financetracker.domain.model.local.Transaction
import com.example.financetracker.domain.model.repository.TransactionRepository
import com.example.financetracker.domain.model.usecase.transaction.UpdateTransactionUseCase
import javax.inject.Inject

class UpdateTransactionUseCaseImpl @Inject constructor(
    private val repository: TransactionRepository
) : UpdateTransactionUseCase {
    override fun execute(transaction: Transaction) {
        repository.updateTransaction(transaction)
    }
}