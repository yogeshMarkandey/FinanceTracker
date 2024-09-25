package com.example.financetracker.data.repository


import com.example.financetracker.data.models.local.dao.BudgetDAO
import com.example.financetracker.data.models.local.dao.CategoryDAO
import com.example.financetracker.data.models.local.dao.CategoryWiseBudgetDAO
import com.example.financetracker.data.models.local.dao.TransactionBudgetCrossDAO
import com.example.financetracker.data.utils.CustomException
import com.example.financetracker.domain.model.local.Budget
import com.example.financetracker.domain.model.local.Budget.Companion.toBudget
import com.example.financetracker.domain.model.local.Budget.Companion.toBudgetEntity
import com.example.financetracker.domain.model.local.Category
import com.example.financetracker.domain.model.local.Category.Companion.toCategory
import com.example.financetracker.domain.model.local.CategoryBudget
import com.example.financetracker.domain.model.local.CategoryBudget.Companion.toCategoryBudget
import com.example.financetracker.domain.model.local.CategoryBudget.Companion.toEntity
import com.example.financetracker.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

class BudgetRepositoryImpl @Inject constructor(
    private val budgetDAO: BudgetDAO,
    private val categoryBudgetDAO: CategoryWiseBudgetDAO,
    private val categoryDAO: CategoryDAO,
    private val transactionCrossRef: TransactionBudgetCrossDAO,
) : BudgetRepository {

    override suspend fun getBudgetsStartBetween(start: Date, end: Date): Flow<List<Budget>> {
        return flow {
            budgetDAO.getStartBetween(start, end).collect { budgetEntities ->
                val budgets = budgetEntities.map { it.toBudget() }
                for (b in budgets) {
                    populateBudgetWithCategoryDetails(budget = b)
                }
                emit(budgets)
            }
        }
    }

    override suspend fun getBudgetById(id: Int): Budget {
        val budget = budgetDAO.getById(id)?.toBudget() ?: throw CustomException("Budget not found")
        populateBudgetWithCategoryDetails(budget)
        return budget
    }

    override suspend fun getBudgetBetween(start: Date, end: Date): List<Budget> {
        val buds = budgetDAO.getBetween(start, end).map { it.toBudget() }
        for (b in buds) {
            populateBudgetWithCategoryDetails(budget = b)
        }
        return buds
    }

    override fun getBudgetsForTransaction(id: Int): List<Budget> {
        val ref = transactionCrossRef.getByTransactionId(id)
        val list = mutableListOf<Budget>()
        ref.forEach {
            val bud = budgetDAO.getById(it.budgetId)?.toBudget()
            if (bud != null) {
                populateBudgetWithCategoryDetails(bud)
                list.add(bud)
            }
        }
        return list
    }

    override fun getBudgetsForDate(date: Date): List<Budget> {
        val budgets = budgetDAO.getBudgetsForDate(date).map { it.toBudget() }
        for (b in budgets) {
            populateBudgetWithCategoryDetails(budget = b)
        }
        return budgets
    }

    private fun populateBudgetWithCategoryDetails(budget: Budget) {
        val catBudsByBudgetModel = categoryBudgetDAO.getAllBy(budget.categoryWiseBudgetIds)
        val allCategories = categoryDAO.getAllCategories()
        val list = catBudsByBudgetModel.map { it.toCategoryBudget() }
        list.forEach { catBud ->
            val category = try {
                categoryDAO.getCategoryById(catBud.categoryId)?.toCategory() ?: Category.na()
            } catch (e: Exception) {
                Category.na()
            }
            catBud.category = category
        }
        val catBuds = list.toMutableList()

        val set = HashSet<Int>()
        catBudsByBudgetModel.forEach { set.add(it.categoryId) }

        allCategories.forEach {
            if (!set.contains(it.id)) {
                set.add(it.id)
                catBuds.add(CategoryBudget.from(budget.id, it.toCategory()))
            }
        }

        budget.categoryBudgets = catBuds
    }

    override fun saveBudget(budget: Budget) {
        val catBudget = budget.categoryBudgets.map { it.toEntity() }
        categoryBudgetDAO.add(catBudget)
        budgetDAO.add(budget.toBudgetEntity())
    }

    override fun updateBudget(budget: Budget) {
        val catBudget = budget.categoryBudgets.map { it.toEntity() }
        categoryBudgetDAO.add(catBudget)
        budgetDAO.update(budget.toBudgetEntity())
    }
}