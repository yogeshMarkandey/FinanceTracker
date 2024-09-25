package com.example.financetracker.data.usecase.budget

import com.example.financetracker.domain.model.local.Budget
import com.example.financetracker.domain.repository.BudgetRepository
import com.example.financetracker.domain.usecase.budget.GetBudgetBetweenUseCase
import java.util.Date
import javax.inject.Inject

class GetBudgetBetweenUseCaseImpl @Inject constructor(
    private val repository: BudgetRepository
) : GetBudgetBetweenUseCase {
    override suspend fun execute(start: Date, end: Date): List<Budget> {
        return repository.getBudgetBetween(start, end)
    }
}