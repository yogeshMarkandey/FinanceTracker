package com.example.financetracker.data.usecase.budget

import com.example.financetracker.domain.model.local.Budget
import com.example.financetracker.domain.repository.BudgetRepository
import com.example.financetracker.domain.usecase.budget.GetBudgetByStartDateUseCase
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject

class GetBudgetByStartDateUseCaseImpl @Inject constructor(
    private val repository: BudgetRepository
) : GetBudgetByStartDateUseCase {
    override suspend fun execute(start: Date, endDate: Date): Flow<List<Budget>> {
        return repository.getBudgetsStartBetween(start, endDate)
    }
}