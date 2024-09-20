package com.example.financetracker.presentation.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.financetracker.data.models.local.PaymentType
import com.example.financetracker.data.utils.CustomException
import com.example.financetracker.domain.model.local.Category
import com.example.financetracker.domain.model.local.StandardColor
import com.example.financetracker.domain.model.usecase.category.AddCategoryUseCase
import com.example.financetracker.domain.model.usecase.category.GetAllCategoryUseCase
import com.example.financetracker.domain.model.usecase.category.GetCategoryByIdUseCase
import com.example.financetracker.domain.model.usecase.category.UpdateCategoryUseCase
import com.example.financetracker.domain.model.usecase.color.GetStandardColorsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class EditCategoryViewModel @Inject constructor(
    private val allCategoryUseCase: GetAllCategoryUseCase,
    private val addCategoryUseCase: AddCategoryUseCase,
    private val getCategoryByIdUseCase: GetCategoryByIdUseCase,
    private val updateCategoryUseCase: UpdateCategoryUseCase,
    private val getStandardColorsUseCase: GetStandardColorsUseCase,
) : ViewModel() {

    private val TAG = this::class.java.name

    private val _allCategories = mutableStateOf(emptyList<Category>())
    private val _isLoading = mutableStateOf(false)
    private val _error = mutableStateOf("")


    val allCategory get() = _allCategories
    val isLoading get() = _isLoading
    val error get() = _error
    val category = mutableStateOf<Category?>(null)
    val categoryTitle = mutableStateOf("")
    val isEditMode = mutableStateOf(false)
    val notesText = mutableStateOf("")
    val paymentType = mutableStateOf(PaymentType.Expense)
    val icon = mutableStateOf("")
    val color = mutableStateOf("")
    val availableColors = mutableStateOf(emptyList<StandardColor>())
    val selectedColor = mutableStateOf(StandardColor.red())

    init {
        getStandardColors()
    }

    fun updateCategoryList(list: List<Category>) {
        _allCategories.value = list
    }

    fun updateCategoryTitle(title: String) {
        categoryTitle.value = title
    }

    fun updateNotesText(value: String) {
        notesText.value = value
    }

    fun updateSelectedColor(color: StandardColor) {
        selectedColor.value = color
    }

    fun addOrEditCategory() {
        val id = category.value?.id ?: System.currentTimeMillis().toInt()

        val category = Category(
            id = id,
            title = categoryTitle.value,
            type = category.value?.type ?: PaymentType.Expense,
            notes = "",
            createdOn = if (isEditMode.value) category.value!!.createdOn else Date(),
            icon = "",
            color = "",
            updatedOn = Date(),
        )

        CoroutineScope(Dispatchers.IO).launch {

            if (isEditMode.value) {
                updateCategoryUseCase.execute(category)
                return@launch
            }

            addCategoryUseCase.execute(category)
        }
    }

    private fun updateLoadingState(bool: Boolean) {
        _isLoading.value = bool
    }

    fun getCategoryById(id: Int) {
        _error.value = ""
        updateLoadingState(true)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = getCategoryByIdUseCase.execute(id)
                category.value = result
                updateCategoryDetails(result)
            } catch (e: CustomException) {
                _error.value = e.errorMessage ?: "Something Went Wrong"
            } catch (e: Exception) {
                _error.value = e.message ?: "Something Went Wrong"
            }
            isEditMode.value = category.value != null
            updateLoadingState(false)
        }
    }

    fun getStandardColors() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                availableColors.value = getStandardColorsUseCase.execute()
            } catch (e: Exception) {
                Log.e(TAG, "getStandardColors: ${e.message}", e)
            }
        }
    }

    private fun updateCategoryDetails(category: Category) {
        categoryTitle.value = category.title
        notesText.value = category.notes
        paymentType.value = category.type
        color.value = category.color
        icon.value = category.icon
        selectedColor.value =
            availableColors.value.firstOrNull {
                it.hex == category.color
            } ?: StandardColor.red()

    }
}