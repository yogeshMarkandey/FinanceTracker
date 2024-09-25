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
import com.example.financetracker.domain.usecase.budget.GetBudgetForDateUseCase
import com.example.financetracker.domain.usecase.transaction.GetAllTransactionBetweenUseCase
import com.example.financetracker.presentation.screens.analysis.AnalysisScreenModes
import com.example.financetracker.presentation.screens.analysis.AnalysisScreenState
import com.example.financetracker.presentation.utils.DateTimeHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class AnalysisScreenViewModel @Inject constructor(
    private val getAllTransactionBetweenUseCase: GetAllTransactionBetweenUseCase,
    private val getBudgetForDateUseCase: GetBudgetForDateUseCase,
    private val getCategoryAnalysisUseCase: GetCategoryAnalysisUseCase,
) : ViewModel() {
    private val TAG = this::class.java.name
    private val currCalendar = mutableStateOf(Calendar.getInstance())
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

    private var initialized = false
    fun initViewModel() {
        if (initialized) {
            return
        }
        initialized = true
        populateStateVariables()
    }

    private fun populateStateVariables() {
        val pair = DateTimeHelper.getMonthStartAndEnd()
        val start = Calendar.getInstance()
        val end = Calendar.getInstance()
        start.time = pair.first
        end.time = pair.second

        setStartAndEndDate(start, end)
    }

    private fun setStartAndEndDate(start: Calendar, end: Calendar) {
        DateTimeHelper.resetStartTime(start)
        DateTimeHelper.resetEndTime(end)
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
                val buds = getBudgetForDateUseCase.execute(start = start, end = end)
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
        currCalendar.value = Calendar.getInstance()
        updateAnalysisDuration(0)
    }


    fun updateAnalysisDuration(offset: Int) {
        val calendar = Calendar.getInstance()
        calendar.time = currCalendar.value.time

        val pair = when (selectedMode.value) {
            AnalysisScreenModes.Month -> {
                DateTimeHelper.getMonthStartAndEnd(calendar.time, offset)
            }

            AnalysisScreenModes.Week -> {
                DateTimeHelper.getWeekStartAndEnd(calendar.time, offset)
            }

            AnalysisScreenModes.Day -> {
                DateTimeHelper.getDayStartAndEnd(calendar.time, offset)
            }

            AnalysisScreenModes.Year -> {
                DateTimeHelper.getYearStartAndEnd(calendar.time, offset)
            }

            else -> {
                DateTimeHelper.getMonthStartAndEnd()
            }
        }
        calendar.time = pair.first
        currCalendar.value = calendar
        val start = Calendar.getInstance()
        start.time = pair.first
        val end = Calendar.getInstance()
        end.time = pair.second
        setStartAndEndDate(start, end)
    }
}