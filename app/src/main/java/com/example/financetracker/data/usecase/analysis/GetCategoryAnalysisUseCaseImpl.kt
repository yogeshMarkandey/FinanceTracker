package com.example.financetracker.data.usecase.analysis

import com.example.financetracker.data.models.local.BudgetType
import com.example.financetracker.domain.model.CategoryAnalysisResult
import com.example.financetracker.domain.repository.BudgetRepository
import com.example.financetracker.domain.repository.TransactionRepository
import com.example.financetracker.domain.usecase.analysis.GetCategoryAnalysisUseCase
import kotlinx.coroutines.flow.first
import java.util.Date
import javax.inject.Inject

class GetCategoryAnalysisUseCaseImpl @Inject constructor(
    private val budgetRepository: BudgetRepository,
    private val transactionRepository: TransactionRepository
) : GetCategoryAnalysisUseCase {
    override suspend fun execute(start: Date, end: Date): List<CategoryAnalysisResult> {
        val categories = transactionRepository.getAllCategories().first()
        val allTransactions = transactionRepository.getAllBetween(start, end).first()
        val budgets = budgetRepository.getBudgetBetween(start, end)

        val budget = budgets.find { it.budgetType == BudgetType.MONTHLY }

        val map = HashMap<Int, Pair<Float, Float>>()
        allTransactions.forEach {
            map[it.categoryId] = Pair((map[it.categoryId]?.first ?: 0f) + it.amount, 0f)
        }
        budget?.categoryBudgets?.forEach {
            val pair = map[it.categoryId]
            map[it.categoryId] = Pair((pair?.first ?: 0f), (pair?.second ?: 0f) + it.amount)
        }

        val list = mutableListOf<CategoryAnalysisResult>()
        categories.forEach {
            val result = CategoryAnalysisResult(
                category = it,
                consumed = map[it.id]?.first ?: 0f,
                limit = map[it.id]?.second ?: 0f
            )
            list.add(result)
        }

        return list
    }
}