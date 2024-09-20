package com.example.financetracker.presentation.viewmodels

import android.icu.util.Calendar
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.financetracker.data.models.local.PaymentType
import com.example.financetracker.domain.model.local.Category
import com.example.financetracker.domain.model.local.Transaction
import com.example.financetracker.domain.model.usecase.category.GetAllCategoryUseCase
import com.example.financetracker.domain.model.usecase.transaction.AddTransactionUseCase
import com.example.financetracker.domain.model.usecase.transaction.GetAllTransactionBetweenUseCase
import com.example.financetracker.domain.model.usecase.transaction.GetTransactionByIdUseCase
import com.example.financetracker.domain.model.usecase.transaction.UpdateTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditTransactionViewModel @Inject constructor(
    private val allCategoryUseCase: GetAllCategoryUseCase,
    private val addTransactionUseCase: AddTransactionUseCase,
    private val updateTransactionUseCase: UpdateTransactionUseCase,
    private val getAllTransactionBetweenUseCase: GetAllTransactionBetweenUseCase,
    private val getTransactionByIdUseCase: GetTransactionByIdUseCase
) : ViewModel() {
    private val TAG = this::class.java.name

    private val _isLoading = mutableStateOf(false)
    private val _allCategories = mutableStateOf(emptyList<Category>())
    private val _selectedCategories = mutableStateOf(Category.other())

    val calendar = mutableStateOf(Calendar.getInstance())
    val amountText = mutableStateOf("0.00")
    val showAmountTextError = mutableStateOf(false)
    val noteText = mutableStateOf("")
    val selectedPaymentType = mutableStateOf(PaymentType.Expense)


    val allCategory get() = _allCategories
    val selectedCategory get() = _selectedCategories

    fun updateAmountText(value: String) {
        val newAmount = value.replace(Regex("[^\\d.]"), "")
        amountText.value = newAmount
        val regex = Regex("^\\d{0,8}(\\.\\d{1,5})?\$")
        showAmountTextError.value = newAmount.isEmpty() || !regex.matches(newAmount)
    }

    fun updateNoteText(value: String) {
        noteText.value = value
    }

    fun updateSelectedPaymentType(type: PaymentType) {
        selectedPaymentType.value = type
    }

    fun selectDate(day: Int, month: Int, year: Int) {
        val newCalendar = Calendar.getInstance()
        newCalendar.set(
            year,
            month,
            day,
            calendar.value.get(Calendar.HOUR),
            calendar.value.get(Calendar.MINUTE)
        )
        calendar.value = newCalendar
    }

    fun selectTime(hour: Int, minute: Int) {
        val newCalendar = Calendar.getInstance()
        newCalendar.set(
            calendar.value.get(Calendar.YEAR),
            calendar.value.get(Calendar.MONTH),
            calendar.value.get(Calendar.DAY_OF_MONTH),
            hour,
            minute
        )
        calendar.value = newCalendar
    }

    fun saveTransaction() {
        val amount = try {
            amountText.value.toFloat()
        } catch (e: Exception) {
            return
        }
        Log.d(TAG, "saveTransaction: Amount: $amount")
        val txn = Transaction(
            id = System.currentTimeMillis().toInt(),
            amount = amount,
            dateTime = calendar.value.time,
            categoryId = selectedCategory.value.id,
            notes = noteText.value,
            paymentType = selectedPaymentType.value,
            tagId = 0,
            paymentSourceId = 0
        )

        CoroutineScope(Dispatchers.IO).launch {
            addTransactionUseCase.execute(txn)
        }
    }


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

    fun updateCategoryList(list: List<Category>) {
        _allCategories.value = list
    }

    fun updateSelectedCategory(category: Category) {
        _selectedCategories.value = category
    }
}