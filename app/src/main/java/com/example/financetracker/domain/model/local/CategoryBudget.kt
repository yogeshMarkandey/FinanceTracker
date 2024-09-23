package com.example.financetracker.domain.model.local

import com.example.financetracker.data.models.local.CategoryWiseBudgetEntity

data class CategoryBudget(
    val id: String,
    val categoryId: Int,
    val budgetId: Int,
    var amount: Float,
    var category: Category = Category.na(),
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

        fun from(budgetId: Int, categoryId: Int): CategoryBudget {
            return CategoryBudget(
                id = "${budgetId}_$categoryId",
                categoryId = categoryId,
                budgetId = budgetId,
                amount = 0f
            )
        }

        fun from(budgetId: Int, category: Category): CategoryBudget {
            return CategoryBudget(
                id = "${budgetId}_${category.id}",
                categoryId = category.id,
                budgetId = budgetId,
                amount = 0f,
                category = category
            )
        }

        fun getDefaultList() = listOf(
            CategoryBudget(
                id = "12_12",
                categoryId = 12,
                budgetId = 12,
                amount = 212f
            ),
            CategoryBudget(
                id = "12_13",
                categoryId = 12,
                budgetId = 13,
                amount = 213f
            ),
            CategoryBudget(
                id = "12_14",
                categoryId = 12,
                budgetId = 14,
                amount = 214f
            )
        )
    }

    fun copyWith(
        amount: Float? = null,
        category: Category? = null,
    ): CategoryBudget {
        return CategoryBudget(
            id = this.id,
            categoryId = categoryId,
            budgetId = budgetId,
            amount = amount ?: this.amount,
            category = category ?: this.category
        )
    }
}
