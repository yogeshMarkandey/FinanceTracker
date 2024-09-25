package com.example.financetracker.domain.model

import com.example.financetracker.domain.model.local.Category

data class CategoryAnalysisResult(
    val category: Category,
    val consumed: Float,
    val limit: Float
) {
    companion object {
        fun getDefaultList(): List<CategoryAnalysisResult> {
            val categories = Category.getDefaults()
            val list = mutableListOf<CategoryAnalysisResult>()

            categories.forEach {
                val catAna = CategoryAnalysisResult(
                    category = it,
                    consumed = 100f,
                    limit = 130f
                )
                list.add(catAna)
            }

            return list
        }
    }

    fun getPercentageConsumed(): Float {
        if (limit == 0f) {
            return consumed
        }

        val percentage = (consumed / limit) * 100f
        return percentage
    }
}
