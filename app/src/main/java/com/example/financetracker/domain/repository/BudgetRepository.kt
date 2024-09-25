package com.example.financetracker.domain.repository

import com.example.financetracker.domain.model.local.Budget
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface BudgetRepository {
    suspend fun getBudgetsStartBetween(start: Date, end: Date): Flow<List<Budget>>
    suspend fun getBudgetById(id: Int): Budget
    suspend fun getBudgetBetween(start: Date, end: Date): List<Budget>
    fun getBudgetsForTransaction(id: Int): List<Budget>
    fun getBudgetsForDate(date: Date): List<Budget>
    fun getBudgetsForDate(start: Date, end: Date): List<Budget>
    fun saveBudget(budget: Budget)
    fun updateBudget(budget: Budget)
}