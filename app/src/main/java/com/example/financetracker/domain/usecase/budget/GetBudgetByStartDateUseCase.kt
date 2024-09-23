package com.example.financetracker.domain.usecase.budget

import com.example.financetracker.domain.model.local.Budget
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface GetBudgetByStartDateUseCase {
    suspend fun execute(start: Date, endDate: Date): Flow<List<Budget>>
}