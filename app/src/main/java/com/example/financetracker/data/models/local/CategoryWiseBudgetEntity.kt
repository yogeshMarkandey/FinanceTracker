package com.example.financetracker.data.models.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categoryBudget")
data class CategoryWiseBudgetEntity(
    @PrimaryKey val id: String,
    val categoryId: Int,
    val budgetId: Int,
    val amount: Float,
)
