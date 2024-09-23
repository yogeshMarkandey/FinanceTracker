package com.example.financetracker.data.usecase.budget

import com.example.financetracker.domain.model.local.Budget
import com.example.financetracker.domain.repository.BudgetRepository
import com.example.financetracker.domain.usecase.budget.GetBudgetByIdUseCase
import javax.inject.Inject

class GetBudgetByIdUseCaseImpl @Inject constructor(
    private val budgetRepository: BudgetRepository,
) : GetBudgetByIdUseCase {
    override suspend fun execute(id: Int): Budget {
        return budgetRepository.getBudgetById(id)
    }
}