package com.example.financetracker.domain.usecase.budget

import com.example.financetracker.domain.model.local.Budget

interface GetBudgetByIdUseCase {
    suspend fun execute(id: Int): Budget
}