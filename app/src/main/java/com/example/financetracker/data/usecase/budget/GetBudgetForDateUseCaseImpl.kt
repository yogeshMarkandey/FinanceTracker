package com.example.financetracker.data.usecase.budget

import com.example.financetracker.domain.model.local.Budget
import com.example.financetracker.domain.repository.BudgetRepository
import com.example.financetracker.domain.usecase.budget.GetBudgetForDateUseCase
import java.util.Date
import javax.inject.Inject

class GetBudgetForDateUseCaseImpl @Inject constructor(
    private val budgetRepository: BudgetRepository,
) : GetBudgetForDateUseCase {
    override fun execute(date: Date): List<Budget> {
        return budgetRepository.getBudgetsForDate(date)
    }

    override fun execute(start: Date, end: Date): List<Budget> {
        return budgetRepository.getBudgetsForDate(start, end)
    }
}