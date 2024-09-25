package com.example.financetracker.domain.usecase.budget

import com.example.financetracker.domain.model.local.Budget
import java.util.Date

interface GetBudgetBetweenUseCase {
    suspend fun execute(start: Date, end: Date) : List<Budget>
}