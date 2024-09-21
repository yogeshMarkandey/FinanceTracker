package com.example.financetracker.domain.usecase.budget

import com.example.financetracker.domain.model.local.Budget

interface SaveBudgetUseCase {
    fun execute(budget: Budget)
}