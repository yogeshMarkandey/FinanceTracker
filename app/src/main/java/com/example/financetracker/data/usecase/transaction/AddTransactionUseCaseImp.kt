package com.example.financetracker.data.usecase.transaction

import com.example.financetracker.domain.model.local.Transaction
import com.example.financetracker.domain.model.repository.TransactionRepository
import com.example.financetracker.domain.model.usecase.transaction.AddTransactionUseCase
import javax.inject.Inject

class AddTransactionUseCaseImp @Inject constructor(
    private val repository: TransactionRepository
) : AddTransactionUseCase {
    override fun execute(transaction: Transaction) {
        repository.addTransaction(transaction)
    }
}