package com.example.financetracker.domain.usecase.budget

import com.example.financetracker.domain.model.local.Budget
import java.util.Date

interface GetBudgetForDateUseCase {
    fun execute(date: Date): List<Budget>
}