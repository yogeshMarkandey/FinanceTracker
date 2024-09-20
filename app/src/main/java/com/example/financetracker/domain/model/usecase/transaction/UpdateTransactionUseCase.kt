package com.example.financetracker.domain.model.usecase.transaction

import com.example.financetracker.domain.model.local.Transaction

interface UpdateTransactionUseCase {
    fun execute(transaction: Transaction)
}