package com.example.financetracker.presentation.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.financetracker.domain.model.local.Budget
import com.example.financetracker.domain.model.local.Transaction
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

    fun initViewModel() {
        populateStateVariables()
    }

    private fun populateStateVariables() {
        val start = Calendar.getInstance()
        start.set(start.get(Calendar.YEAR), start.get(Calendar.MONTH), 1)

        val end = Calendar.getInstance()
        end.set(start.get(Calendar.YEAR), start.get(Calendar.MONTH), 1)
        val lastDay = end.getActualMaximum(Calendar.DAY_OF_MONTH)
        end.set(Calendar.DAY_OF_MONTH, lastDay)

        setStartAndEndDate(start, end)
    }

    private fun setStartAndEndDate(start: Calendar, end: Calendar) {
        startCalender.value = start
        endCalendar.value = end
        loadData()
    }

    private fun loadData() {
        CoroutineScope(Dispatchers.IO).launch {
            setState(AnalysisScreenState.LOADING)

            try {
                val buds = getBudgetBetweenUseCase.execute(
                    start = startCalender.value.time,
                    end = endCalendar.value.time
                )
                updateActiveBudgetList(buds)
                setState(AnalysisScreenState.LOAD_DATA_SUCCESS)
            } catch (e: Exception) {
                showError(message = e.message)
                setState(AnalysisScreenState.LOAD_DATA_ERROR)
                Log.e(TAG, "loadData: ", e)
            }
        }
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