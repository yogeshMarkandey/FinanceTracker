package com.example.financetracker.domain.model.local

import com.example.financetracker.data.models.local.CategoryWiseBudgetEntity

data class CategoryBudget(
    val id: String,
    val categoryId: Int,
    val budgetId: Int,
    val amount: Float,
) {
    companion object {
        fun CategoryBudget.toEntity(): CategoryWiseBudgetEntity {
            return CategoryWiseBudgetEntity(
                id = id,
                categoryId = categoryId,
                budgetId = budgetId,
                amount = amount
            )
        }

        fun CategoryWiseBudgetEntity.toCategoryBudget(): CategoryBudget {
            return CategoryBudget(
                id = id,
                categoryId = categoryId,
                budgetId = budgetId,
                amount = amount
            )
        }
    }
}
