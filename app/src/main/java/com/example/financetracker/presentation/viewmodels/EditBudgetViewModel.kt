package com.example.financetracker.presentation.viewmodels

import android.icu.util.Calendar
import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.financetracker.data.utils.CustomException
import com.example.financetracker.domain.model.local.Budget
import com.example.financetracker.domain.model.local.Category
import com.example.financetracker.domain.model.local.CategoryBudget
import com.example.financetracker.domain.usecase.budget.GetBudgetByIdUseCase
import com.example.financetracker.domain.usecase.budget.SaveBudgetUseCase
import com.example.financetracker.domain.usecase.budget.UpdateBudgetUseCase
import com.example.financetracker.domain.usecase.category.GetAllCategoryUseCase
import com.example.financetracker.presentation.screens.home.states.EditBudgetScreenStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class EditBudgetViewModel @Inject constructor(
    private val saveBudgetUseCase: SaveBudgetUseCase,
    private val updateBudgetUseCase: UpdateBudgetUseCase,
    private val getBudgetByIdUseCase: GetBudgetByIdUseCase,
    private val getAllCategoryUseCase: GetAllCategoryUseCase,
) : ViewModel() {
    private val TAG = this::class.java.name

    private val _loadedBudget = mutableStateOf(Budget.newInstance())
    private val _allCategories = mutableStateOf(emptyList<Category>())

    val errorMessage = mutableStateOf("")
    val showToastError = mutableIntStateOf(0)
    val isEditMode = mutableStateOf(false)
    val screenStates = mutableStateOf(EditBudgetScreenStates.INITIAL)
    val budgetAmountError = mutableStateOf("")
    val budgetAmount = mutableStateOf("0")
    val categoryBudgets = mutableStateListOf<CategoryBudget>()
    val budgetTitle = mutableStateOf("")
    val startCalender = mutableStateOf(Calendar.getInstance())
    val endCalender = mutableStateOf(Calendar.getInstance())
    val balanceAmount = mutableStateOf(0f)

    fun saveBudget() {
        if (budgetAmountError.value.isNotBlank()) {
            showError("Please enter valid budget amount")
            setState(EditBudgetScreenStates.SAVE_BUDGET_ERROR)
            return
        }
        setState(EditBudgetScreenStates.LOADING)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val budget = Budget(
                    id = if (isEditMode.value) _loadedBudget.value.id
                    else System.currentTimeMillis().toInt(),
                    title = budgetTitle.value,
                    startDate = startCalender.value.time,
                    endDate = endCalender.value.time,
                    budgetAmount = budgetAmount.value.toFloat(),
                    categoryWiseBudgetIds = arrayListOf(),
                    categoryBudgets = categoryBudgets,
                    totalConsumed = 0f,
                )
                budget.categoryBudgets.forEach {
                    budget.categoryWiseBudgetIds.add(it.id)
                }
                if (isEditMode.value) {
                    updateBudgetUseCase.execute(budget)
                } else {
                    saveBudgetUseCase.execute(budget)
                }
                Log.d(TAG, "saveBudget: Budget Saved Successfully")
                setState(EditBudgetScreenStates.SAVE_BUDGET_SUCCESS)
            } catch (e: Exception) {
                showError(e.message ?: "An Error Occurred while saving budget.")
                setState(EditBudgetScreenStates.SAVE_BUDGET_ERROR)
                Log.e(TAG, "saveBudget: Error saving Budget", e)
            }
        }
    }

    private fun setState(states: EditBudgetScreenStates) {
        screenStates.value = states
    }

    private fun showError(message: String) {
        errorMessage.value = message
        showToastError.intValue += 1
    }

    fun loadAllCategories() {
        setState(EditBudgetScreenStates.LOADING)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                getAllCategoryUseCase.execute().collect { categories ->
                    _allCategories.value = categories
                    setState(EditBudgetScreenStates.LOAD_ALL_CATEGORY_SUCCESS)
                }
            } catch (e: Exception) {
                showError("Unable to load Categories.")
                setState(EditBudgetScreenStates.LOAD_ALL_CATEGORY_ERROR)
                Log.e(TAG, "loadAllCategories: Error Loading All Categories", e)
            }
        }
    }

    private fun populateCategoryBudget(budget: Budget) {
        _allCategories.value.forEach {
            val catBud = CategoryBudget.from(budget.id, it.id)
            catBud.category = it
            budget.categoryBudgets.add(catBud)
        }
    }

    fun loadBudgetById(id: Int) {
        setState(EditBudgetScreenStates.LOADING)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val budget = getBudgetByIdUseCase.execute(id)
                _loadedBudget.value = budget
                setState(EditBudgetScreenStates.LOAD_BUDGET_SUCCESS)
                isEditMode.value = true
                initStateVariables(budget)
            } catch (e: CustomException) {
                showError(e.errorMessage)
                isEditMode.value = false
                Log.e(TAG, "loadBudgetById: Error loading budget by Id: $id", e)
                getNewBudgetInstance()
            } catch (e: Exception) {
                showError(e.message ?: "Something Went Wrong")
                isEditMode.value = false
                Log.e(TAG, "loadBudgetById: Error loading budget by Id: $id", e)
                getNewBudgetInstance()
            }
        }
    }

    private fun initStateVariables(budget: Budget) {
        budgetAmount.value = budget.budgetAmount.toString()
        budgetTitle.value = budget.title
        val startCalender = Calendar.getInstance()
        startCalender.time = budget.startDate

        val endCalender = Calendar.getInstance()
        endCalender.time = budget.endDate
        this.startCalender.value = startCalender
        this.endCalender.value = endCalender
        categoryBudgets.addAll(budget.categoryBudgets.toMutableList())
        var maxBudget = budgetAmount.value.toFloat()
        categoryBudgets.forEach {
            maxBudget -= it.amount
        }
        balanceAmount.value = maxBudget

    }

    fun getNewBudgetInstance() {
        val newInstance = Budget.newInstance()
        populateCategoryBudget(newInstance)
        _loadedBudget.value = newInstance
        initStateVariables(newInstance)
    }

    fun updateCategoryBudgetAmount(value: String, index: Int) {
        val oldValue = categoryBudgets[index].amount
        var newAmount = value.replace(Regex("[^\\d.]"), "")
        val regex = Regex("^\\d{0,8}(\\.\\d{1,5})?\$")

        if (newAmount.isBlank()) {
            newAmount = "0"
        }

        if (!regex.matches(newAmount)) {
            showError("$value is not a valid number.")
            newAmount = oldValue.toString()
        }

        val floatValue = newAmount.toFloat()

        val balance = balanceAmount.value + oldValue - floatValue

        if (balance < 0) {
            showError("Budget amount is exceeded.")
            newAmount = oldValue.toString()
        }

        balanceAmount.value = balance
        val catBud = categoryBudgets[index].copyWith(amount = newAmount.toFloat())
        categoryBudgets[index] = catBud
    }

    fun updateBudgetAmount(value: String) {
        budgetAmountError.value = ""
        var newAmount = value.replace(Regex("[^\\d.]"), "")
        val regex = Regex("^\\d{0,8}(\\.\\d{1,5})?\$")

        if (!regex.matches(newAmount)) {
            budgetAmountError.value = "$value is not a valid number."
        }

        budgetAmount.value = newAmount

        if (newAmount.isBlank()){
            newAmount = "0"
        }

        var balance = newAmount.toFloat()
        categoryBudgets.forEach {
            balance -= it.amount
        }
        balanceAmount.value = balance
    }

    fun onValidateCategoryAmountInput(value: String, index: Int): String {
        val oldValue = categoryBudgets[index].amount
        if (value.isBlank()) {
            return ""
        }

        val newAmount = value.replace(Regex("[^\\d.]"), "")
        val regex = Regex("^\\d{0,8}(\\.\\d{1,5})?\$")

        if (!regex.matches(newAmount)) {
            return "Please enter valid number."
        }

        val floatValue = newAmount.toFloat()

        val balance = balanceAmount.value + oldValue - floatValue

        if (balance < 0) {
            return "Budget amount is exceeded."
        }

        return ""
    }

    fun getReadableStartDate(): String {
        val format = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
        return try {
            format.format(startCalender.value.time)
        } catch (e: Exception) {
            Log.e(TAG, "getReadableStartDate: Error parsing start date", e)
            "NA"
        }
    }

    fun getReadableEmdDate(): String {
        val format = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
        return try {
            format.format(endCalender.value.time)
        } catch (e: Exception) {
            Log.e(TAG, "getReadableStartDate: Error parsing start date", e)
            "NA"
        }
    }

    fun updateBudgetTitle(value: String) {
        budgetTitle.value = value
    }

    fun setStartDate(day: Int, month: Int, year: Int) {
        val newCalendar = Calendar.getInstance()
        newCalendar.set(
            year,
            month,
            day,
        )
        startCalender.value = newCalendar
    }

    fun setEndDate(day: Int, month: Int, year: Int) {
        val newCalendar = Calendar.getInstance()
        newCalendar.set(
            year,
            month,
            day,
        )

        if (newCalendar.time.before(startCalender.value.time)) {
            showError("End date is After start date.\nPlease select valid dates.")
            return
        }

        endCalender.value = newCalendar
    }
}