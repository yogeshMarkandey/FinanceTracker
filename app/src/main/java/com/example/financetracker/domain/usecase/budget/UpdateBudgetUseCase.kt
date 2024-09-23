package com.example.financetracker.domain.usecase.budget

import com.example.financetracker.domain.model.local.Budget

interface UpdateBudgetUseCase {
    fun execute(budget: Budget)
}