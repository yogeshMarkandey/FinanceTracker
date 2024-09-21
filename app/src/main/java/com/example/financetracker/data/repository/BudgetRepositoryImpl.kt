package com.example.financetracker.data.repository


import com.example.financetracker.data.models.local.dao.BudgetDAO
import com.example.financetracker.data.models.local.dao.CategoryWiseBudgetDAO
import com.example.financetracker.domain.model.local.Budget
import com.example.financetracker.domain.model.local.Budget.Companion.toBudget
import com.example.financetracker.domain.model.local.Budget.Companion.toBudgetEntity
import com.example.financetracker.domain.model.local.CategoryBudget.Companion.toCategoryBudget
import com.example.financetracker.domain.model.local.CategoryBudget.Companion.toEntity
import com.example.financetracker.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Date
import javax.inject.Inject

class BudgetRepositoryImpl @Inject constructor(
    private val budgetDAO: BudgetDAO,
    private val categoryBudgetDAO: CategoryWiseBudgetDAO
) : BudgetRepository {
    override suspend fun getBudgetsStartBetween(start: Date, end: Date): Flow<List<Budget>> {
        return flow {
            budgetDAO.getStartBetween(start, end).collect { budgetEntities ->
                val budgets = budgetEntities.map { it.toBudget() }
                for (b in budgets) {
                    val categoryBudgets = categoryBudgetDAO.getAllBy(b.categoryWiseBudgetIds)
                    b.categoryBudgets =
                        categoryBudgets.map { it.toCategoryBudget() }.toMutableList()
                }
                emit(budgets)
            }
        }
    }

    override fun saveBudget(budget: Budget) {
        val catBudget = budget.categoryBudgets.map { it.toEntity() }
        categoryBudgetDAO.add(catBudget)
        budgetDAO.add(budget.toBudgetEntity())
    }

    override fun updateBudget(budget: Budget) {
        budgetDAO.update(budget.toBudgetEntity())
    }
}