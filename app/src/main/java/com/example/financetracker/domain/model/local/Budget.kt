package com.example.financetracker.domain.model.local

import android.util.Log
import com.example.financetracker.data.models.local.BudgetEntity
import com.example.financetracker.data.models.local.BudgetType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Budget(
    val id: Int,
    var startDate: Date,
    var endDate: Date,
    var title: String,
    var budgetAmount: Float,
    var totalConsumed: Float,
    var categoryWiseBudgetIds: ArrayList<String>,
    var budgetType: BudgetType,
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
                categoryWiseBudgetIds = categoryWiseBudgetIds,
                budgetType = budgetType,
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
                categoryWiseBudgetIds = categoryWiseBudgetIds,
                budgetType = budgetType,
            )
        }

        fun getDefault() = Budget(
            id = System.currentTimeMillis().toInt(),
            startDate = Date(),
            endDate = Date(),
            title = "Monthly Budget",
            budgetAmount = 2000f,
            totalConsumed = 100f,
            categoryWiseBudgetIds = ArrayList(),
            budgetType = BudgetType.MONTHLY
        )

        fun newInstance(): Budget {
            val startDate = Date()
            val format = SimpleDateFormat("MMMM", Locale.ENGLISH)

            return Budget(
                id = System.currentTimeMillis().toInt(),
                startDate = startDate,
                endDate = Date(),
                title = "${format.format(startDate)} Budget",
                budgetAmount = 0f,
                totalConsumed = 0f,
                categoryWiseBudgetIds = ArrayList(),
                budgetType = BudgetType.NA
            )
        }
    }

    private val TAG = this::class.java.name

    fun getReadableStartDate(): String {
        val format = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
        return try {
            format.format(startDate)
        } catch (e: Exception) {
            Log.e(TAG, "getReadableStartDate: Error parsing start date", e)
            "NA"
        }
    }

    fun getReadableEndDate(): String {
        val format = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
        return try {
            format.format(endDate)
        } catch (e: Exception) {
            Log.e(TAG, "getReadableStartDate: Error parsing end date", e)
            "NA"
        }
    }

    fun getBudgetDuration(): String {
        return "${getReadableStartDate()}-${getReadableEndDate()}"
    }
}
