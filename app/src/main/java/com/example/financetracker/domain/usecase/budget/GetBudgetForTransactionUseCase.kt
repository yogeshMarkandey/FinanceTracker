package com.example.financetracker.domain.usecase.budget

import com.example.financetracker.domain.model.local.Budget

interface GetBudgetForTransactionUseCase {
    fun execute(transactionId: Int) : List<Budget>
}