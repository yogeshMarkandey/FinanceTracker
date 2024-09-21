package com.example.financetracker.domain.model.local

import com.example.financetracker.data.models.local.BudgetEntity
import java.util.Date

data class Budget(
    val id: Int,
    val startDate: Date,
    val endDate: Date,
    val title: String,
    val budgetAmount: Float,
    val totalConsumed: Float,
    val categoryWiseBudgetIds: ArrayList<String>,
    var categoryBudgets: MutableList<CategoryBudget> = mutableListOf()
) {
    companion object {
        fun BudgetEntity.toBudget(): Budget {
            return Budget(
                id = id,
                startDate = startDate,
                endDate = endDate,
                title = title,
                budgetAmount = budgetAmount,
                totalConsumed = totalConsumed,
                categoryWiseBudgetIds = categoryWiseBudgetIds
            )
        }

        fun Budget.toBudgetEntity(): BudgetEntity {
            return BudgetEntity(
                id = id,
                startDate = startDate,
                endDate = endDate,
                title = title,
                budgetAmount = budgetAmount,
                totalConsumed = totalConsumed,
                categoryWiseBudgetIds = categoryWiseBudgetIds
            )
        }
    }
}
