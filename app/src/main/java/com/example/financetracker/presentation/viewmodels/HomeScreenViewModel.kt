package com.example.financetracker.presentation.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.financetracker.domain.model.local.Category
import com.example.financetracker.domain.model.local.Transaction
import com.example.financetracker.domain.model.usecase.category.GetCategoryByIdUseCase
import com.example.financetracker.domain.model.usecase.transaction.GetAllTransactionBetweenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getAllTransactionBetweenUseCase: GetAllTransactionBetweenUseCase,
    private val getCategoryByIdUseCase: GetCategoryByIdUseCase
) : ViewModel() {
    private val TAG = this::class.java.name
    private val _latestTransaction = mutableStateOf(emptyList<Transaction>())
    private val _isLoading = mutableStateOf(false)
    val isLoading get() = _isLoading
    val latestTransaction get() = _latestTransaction


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
                for (i in it) {
                    i.category = getCategoryDetailsById(i.categoryId)
                }
                _latestTransaction.value = it
                _isLoading.value = false
            }
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