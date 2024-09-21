package com.example.financetracker.domain.usecase.transaction

import com.example.financetracker.domain.model.local.Transaction

interface AddTransactionUseCase {
    fun execute(transaction: Transaction)
}