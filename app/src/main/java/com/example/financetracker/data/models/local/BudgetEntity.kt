package com.example.financetracker.data.models.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "budget")
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val startDate: Date,
    val endDate: Date,
    val title: String,
    val budgetAmount: Float,
    val totalConsumed: Float,
    val categoryWiseBudgetIds: ArrayList<String>
)