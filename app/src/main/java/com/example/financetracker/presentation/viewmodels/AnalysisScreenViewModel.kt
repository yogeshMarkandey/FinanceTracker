package com.example.financetracker.presentation.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.financetracker.domain.model.CategoryAnalysisResult
import com.example.financetracker.domain.model.local.Budget
import com.example.financetracker.domain.model.local.Transaction
import com.example.financetracker.domain.usecase.analysis.GetCategoryAnalysisUseCase
import com.example.financetracker.domain.usecase.budget.GetBudgetBetweenUseCase
import com.example.financetracker.domain.usecase.transaction.GetAllTransactionBetweenUseCase
import com.example.financetracker.presentation.screens.analysis.AnalysisScreenModes
import com.example.financetracker.presentation.screens.analysis.AnalysisScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class AnalysisScreenViewModel @Inject constructor(
    private val getAllTransactionBetweenUseCase: GetAllTransactionBetweenUseCase,
    private val getBudgetBetweenUseCase: GetBudgetBetweenUseCase,
    private val getCategoryAnalysisUseCase: GetCategoryAnalysisUseCase,
) : ViewModel() {
    private val TAG = this::class.java.name
    val transactions = mutableStateListOf<Transaction>()
    val startCalender = mutableStateOf(Calendar.getInstance())
    val endCalendar = mutableStateOf(Calendar.getInstance())
    val availableAnalysisScreenModes = mutableStateOf(AnalysisScreenModes.entries.toList())
    val selectedMode = mutableStateOf(AnalysisScreenModes.Month)
    val activeBudgets = mutableStateListOf<Budget>()
    val screenState = mutableStateOf(AnalysisScreenState.INITIAL)
    val errorMessage = mutableStateOf("")
    val showToastState = mutableIntStateOf(0)
    val categoryAnalysisResults = mutableStateListOf<CategoryAnalysisResult>()

    fun initViewModel() {
        populateStateVariables()
    }

    private fun populateStateVariables() {
        val start = Calendar.getInstance()
        start.set(start.get(Calendar.YEAR), start.get(Calendar.MONTH), 1)
        start.set(Calendar.HOUR_OF_DAY, 0)

        val end = Calendar.getInstance()
        end.set(start.get(Calendar.YEAR), start.get(Calendar.MONTH), 1)
        val lastDay = end.getActualMaximum(Calendar.DAY_OF_MONTH)
        end.set(Calendar.DAY_OF_MONTH, lastDay)

        setStartAndEndDate(start, end)
    }

    private fun setStartAndEndDate(start: Calendar, end: Calendar) {
        start.set(Calendar.DAY_OF_MONTH, 1)
        start.set(Calendar.MINUTE, 0)
        start.set(Calendar.SECOND, 0)
        start.set(Calendar.MILLISECOND, 0)
        end.set(Calendar.HOUR_OF_DAY, 23)
        end.set(Calendar.MINUTE, 59)
        end.set(Calendar.SECOND, 59)
        end.set(Calendar.MILLISECOND, 59)
        startCalender.value = start
        endCalendar.value = end
        loadData()
    }

    private fun loadData() {
        CoroutineScope(Dispatchers.IO).launch {
            setState(AnalysisScreenState.LOADING)

            try {
                val start = startCalender.value.time
                val end = endCalendar.value.time
                val buds = getBudgetBetweenUseCase.execute(start = start, end = end)
                updateActiveBudgetList(buds)

                val catAnalysis = getCategoryAnalysisUseCase.execute(start = start, end = end)
                updateCatAnalysisResults(catAnalysis)

                setState(AnalysisScreenState.LOAD_DATA_SUCCESS)
            } catch (e: Exception) {
                showError(message = e.message)
                setState(AnalysisScreenState.LOAD_DATA_ERROR)
                Log.e(TAG, "loadData: ", e)
            }
        }
    }

    private fun updateCatAnalysisResults(catAnalysis: List<CategoryAnalysisResult>) {
        categoryAnalysisResults.clear()
        categoryAnalysisResults.addAll(catAnalysis)
    }

    private fun showError(message: String?) {
        errorMessage.value = message ?: "Something went wrong"
        showToastState.intValue += 1
    }

    private fun updateActiveBudgetList(buds: List<Budget>) {
        activeBudgets.clear()
        activeBudgets.addAll(buds)
    }

    private fun setState(state: AnalysisScreenState) {
        screenState.value = state
    }

    fun updateSelectedAnalysisMode(modes: AnalysisScreenModes) {
        selectedMode.value = modes
    }

}