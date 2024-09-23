package com.example.financetracker.data.usecase.budget

import com.example.financetracker.domain.model.local.Budget
import com.example.financetracker.domain.repository.BudgetRepository
import com.example.financetracker.domain.usecase.budget.UpdateBudgetUseCase
import javax.inject.Inject

class UpdateBudgetUseCaseImpl @Inject constructor(
    private val budgetRepository: BudgetRepository
) : UpdateBudgetUseCase {
    override fun execute(budget: Budget) {
        budgetRepository.updateBudget(budget)
    }
}