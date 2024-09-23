package com.example.financetracker.presentation.viewmodels

import android.icu.util.Calendar
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.financetracker.data.models.local.PaymentType
import com.example.financetracker.domain.model.local.Category
import com.example.financetracker.domain.model.local.Transaction
import com.example.financetracker.domain.usecase.category.GetAllCategoryUseCase
import com.example.financetracker.domain.usecase.category.GetCategoryByIdUseCase
import com.example.financetracker.domain.usecase.transaction.AddTransactionUseCase
import com.example.financetracker.domain.usecase.transaction.GetAllTransactionBetweenUseCase
import com.example.financetracker.domain.usecase.transaction.GetTransactionByIdUseCase
import com.example.financetracker.domain.usecase.transaction.UpdateTransactionUseCase
import com.example.financetracker.presentation.screens.home.states.EditTransactionScreenStates
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
    private val getTransactionByIdUseCase: GetTransactionByIdUseCase,
    private val getCategoryByIdUseCase: GetCategoryByIdUseCase,
) : ViewModel() {
    private val TAG = this::class.java.name

    private val _errorMessage = mutableStateOf("")
    private val _screenStates = mutableStateOf(EditTransactionScreenStates.INITIAL)
    private val _allCategories = mutableStateOf(emptyList<Category>())
    private val _selectedCategories = mutableStateOf(Category.other())
    private val _loadedTransaction = mutableStateOf<Transaction?>(null)

    val calendar = mutableStateOf(Calendar.getInstance())
    val amountText = mutableStateOf("0.00")
    val showAmountTextError = mutableStateOf(false)
    val noteText = mutableStateOf("")
    val selectedPaymentType = mutableStateOf(PaymentType.Expense)


    val allCategory get() = _allCategories
    val selectedCategory get() = _selectedCategories
    val errorMessage get() = _errorMessage
    val screenStates get() = _screenStates

    val isEditMode = mutableStateOf(false)

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

    private var tryingToSave = false
    fun saveTransaction() {
        if (tryingToSave) {
            return
        }
        tryingToSave = true
        _screenStates.value = EditTransactionScreenStates.LOADING
        val amount = try {
            amountText.value.toFloat()
        } catch (e: Exception) {
            _errorMessage.value = e.message ?: "Something went wrong!"
            _screenStates.value = EditTransactionScreenStates.UPDATE_ERROR
            tryingToSave = false
            return
        }

        val id =
            if (isEditMode.value)
                _loadedTransaction.value!!.id
            else System.currentTimeMillis().toInt()

        val txn = Transaction(
            id = id,
            amount = amount,
            dateTime = calendar.value.time,
            categoryId = selectedCategory.value.id,
            notes = noteText.value,
            paymentType = selectedPaymentType.value,
            tagId = 0,
            paymentSourceId = 0
        )

        CoroutineScope(Dispatchers.IO).launch {
            if (isEditMode.value) {
                updateTransactionUseCase.execute(txn)
            } else {
                addTransactionUseCase.execute(txn)
            }
            _screenStates.value = EditTransactionScreenStates.UPDATE_SUCCESS
            tryingToSave = false
        }
    }


    fun initViewModel() {
        Log.d(TAG, "Init Called: ")
        CoroutineScope(Dispatchers.IO).launch {
            allCategoryUseCase.execute().collect { list ->
                updateCategoryList(list.ifEmpty { Category.getDefaults() })
            }
        }
    }

    fun loadTransactionById(id: Int) {
        _screenStates.value = EditTransactionScreenStates.LOADING
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val transaction = getTransactionByIdUseCase.execute(id)
                if (transaction == null) {
                    errorMessage.value = "Transaction Not Found"
                    _screenStates.value = EditTransactionScreenStates.LOAD_TRANSACTION_ERROR
                    return@launch
                }
                isEditMode.value = true
                populateTransactionDetails(transaction)
                _loadedTransaction.value = transaction
                _screenStates.value = EditTransactionScreenStates.LOAD_TRANSACTION_SUCCESS
            } catch (e: Exception) {
                errorMessage.value = e.message ?: "Error while loading transaction"
                screenStates.value = EditTransactionScreenStates.LOAD_TRANSACTION_ERROR
                isEditMode.value = false
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

    private fun populateTransactionDetails(transaction: Transaction) {
        updateAmountText("${transaction.amount}")
        updateNoteText(transaction.notes)
        updateSelectedPaymentType(transaction.paymentType)
        val category = getCategoryDetailsById(transaction.categoryId)
        val calendar = Calendar.getInstance()
        calendar.time = transaction.dateTime
        this.calendar.value = calendar
        updateSelectedCategory(category)
    }

    fun updateCategoryList(list: List<Category>) {
        _allCategories.value = list
    }

    fun updateSelectedCategory(category: Category) {
        _selectedCategories.value = category
    }
}