package com.example.financetracker.presentation.viewmodels

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


    fun initViewModel() {
        Log.d(TAG, "Init Called: ")
        CoroutineScope(Dispatchers.IO).launch {
            allCategoryUseCase.execute().collect { list ->
                updateCategoryList(list.ifEmpty { Category.getDefaults() })
                if (selectedCategory.value == null) {
                    updateSelectedCategory(_allCategories.value[0])
                }
                Log.d(TAG, "Called: list length: ${list.size}")
            }
        }
    }

    fun setLoadingState(bool: Boolean) {
        _isLoading.value = bool
    }


    fun updateCategoryList(list: List<Category>) {
        _allCategories.value = list
    }

    fun updateSelectedCategory(category: Category) {
        _selectedCategories.value = category
    }

    fun updateCategory(category: Category) {
        CoroutineScope(Dispatchers.IO).launch {
            addCategoryUseCase.execute(category)
        }
    }
}