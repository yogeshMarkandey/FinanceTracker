package com.example.financetracker.data.usecase.budget

import com.example.financetracker.domain.model.local.Budget
import com.example.financetracker.domain.repository.BudgetRepository
import com.example.financetracker.domain.usecase.budget.SaveBudgetUseCase
import javax.inject.Inject

class SaveBudgetUseCaseImpl @Inject constructor(
    private val budgetRepository: BudgetRepository
) : SaveBudgetUseCase {
    override fun execute(budget: Budget) {
        budgetRepository.saveBudget(budget)
    }
}