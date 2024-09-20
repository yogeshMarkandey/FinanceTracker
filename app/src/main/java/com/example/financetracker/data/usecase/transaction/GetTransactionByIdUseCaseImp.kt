package com.example.financetracker.data.usecase.transaction

import com.example.financetracker.domain.model.local.Transaction
import com.example.financetracker.domain.model.repository.TransactionRepository
import com.example.financetracker.domain.model.usecase.transaction.GetTransactionByIdUseCase
import javax.inject.Inject

class GetTransactionByIdUseCaseImp @Inject constructor(
    private val repository: TransactionRepository
) : GetTransactionByIdUseCase {
    override fun execute(id: Int): Transaction? {
        return repository.getTransactionById(id)
    }
}