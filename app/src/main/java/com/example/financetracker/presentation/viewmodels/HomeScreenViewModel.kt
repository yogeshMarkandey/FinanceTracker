package com.example.financetracker.presentation.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.financetracker.data.models.local.PaymentType
import com.example.financetracker.domain.model.local.Budget
import com.example.financetracker.domain.model.local.Category
import com.example.financetracker.domain.model.local.Transaction
import com.example.financetracker.domain.usecase.budget.GetBudgetByStartDateUseCase
import com.example.financetracker.domain.usecase.category.GetCategoryByIdUseCase
import com.example.financetracker.domain.usecase.transaction.GetAllTransactionBetweenUseCase
import com.example.financetracker.presentation.utils.DateTimeHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getAllTransactionBetweenUseCase: GetAllTransactionBetweenUseCase,
    private val getCategoryByIdUseCase: GetCategoryByIdUseCase,
    private val getBudgetByStartDateUseCase: GetBudgetByStartDateUseCase,
) : ViewModel() {

    private val TAG = this::class.java.name

    private val _latestTransaction = mutableStateOf(emptyList<Transaction>())
    private val _isLoading = mutableStateOf(false)
    private val _activeBudgets = mutableStateOf(emptyList<Budget>())

    val isLoading get() = _isLoading
    val latestTransaction get() = _latestTransaction
    val balanceAmount = mutableFloatStateOf(0.0f)
    val spendingAmount = mutableFloatStateOf(0.0f)
    val incomeAmount = mutableFloatStateOf(0.0f)
    val activeBudgets get() = _activeBudgets


    fun loadLatestTransaction() {
        _isLoading.value = true
        val calendar = Calendar.getInstance()
        val startCalender = Calendar.getInstance()
        startCalender.add(Calendar.DAY_OF_MONTH, -7)
        CoroutineScope(Dispatchers.IO).launch {
            getAllTransactionBetweenUseCase.execute(
                start = startCalender.time,
                end = calendar.time
            ).collect {
                addCategoryDetailsEachTransaction(it)
                updateMonthSummary(it)
                _latestTransaction.value = it
                _isLoading.value = false
            }
        }
    }

    fun loadBudgetByStartDate() {
        CoroutineScope(Dispatchers.IO).launch {
            val pair = DateTimeHelper.getMonthStartAndEnd()
            val start = Calendar.getInstance()
            val end = Calendar.getInstance()
            start.time = pair.first
            end.time = pair.second

            getBudgetByStartDateUseCase.execute(start.time, end.time).collect {
                _activeBudgets.value = it
            }
        }
    }


    private fun updateMonthSummary(transactions: List<Transaction>) {
        var income = 0.0f
        var expense = 0.0f
        transactions.forEach { transaction ->
            if (transaction.paymentType == PaymentType.Expense) {
                expense += transaction.amount
            } else {
                income += transaction.amount
            }
        }

        val balance: Float = income - expense

        balanceAmount.floatValue = balance
        incomeAmount.floatValue = income
        spendingAmount.floatValue = expense

    }

    private fun addCategoryDetailsEachTransaction(transactions: List<Transaction>) {
        for (i in transactions) {
            i.category = getCategoryDetailsById(i.categoryId)
        }
    }

    private fun getCategoryDetailsById(id: Int): Category {
        return try {
            getCategoryByIdUseCase.execute(id)
        } catch (e: Exception) {
            Log.e(TAG, "getCategoryDetailsById: Error: Getting Category by Id: $id", e)
            Category.newInstance()
        }
    }
}