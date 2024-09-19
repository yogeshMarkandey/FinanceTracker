package com.example.financetracker.presentation.viewmodels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.financetracker.data.models.local.TransactionModel
import com.example.financetracker.domain.model.local.Category
import com.example.financetracker.domain.model.usecase.category.AddCategoryUseCase
import com.example.financetracker.domain.model.usecase.category.GetAllCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val addCategoryUseCase: AddCategoryUseCase,
    private val allCategoryUseCase: GetAllCategoryUseCase
) : ViewModel() {
    private val TAG = this::class.java.name

    private val _isLoading = mutableStateOf(false)
    private val _txnState = mutableStateOf(emptyList<TransactionModel>())
    private val _allCategories = mutableStateOf(emptyList<Category>())
    private val _selectedCategories = mutableStateOf<Category?>(null)


    val txnState get() = _txnState
    val isLoading get() = _isLoading
    val allCategory get() = _allCategories
    val selectedCategory get() = _selectedCategories


    init {
        updateCategoryList(Category.getDefaults())
        updateSelectedCategory(_allCategories.value[0])
        CoroutineScope(Dispatchers.IO).launch {
            allCategoryUseCase.execute().collect { list ->
                updateCategoryList(list)
                Log.d(TAG, "Called: list length: ${list.size}")
            }
        }
    }

    fun getDataFromFile(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
//            _txnState.value = readExcelFile(context)
        }
    }

//    fun readExcelFile(context: Context): List<TransactionModel> {
//        setLoadingState(true)
//        val inputStream = context.resources.openRawResource(R.raw.feb_2023_2)
//
//
//        setLoadingState(false)
//        return transactionRepository.getTransactionFromExcelFile(inputStream)
//    }


    fun setLoadingState(bool: Boolean) {
        _isLoading.value = bool
    }


    fun updateCategoryList(list: List<Category>) {
        _allCategories.value = list
    }

    fun updateSelectedCategory(category: Category) {
        _selectedCategories.value = category
    }
}