package com.example.financetracker.presentation.screens.home

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.financetracker.domain.model.local.CategoryBudget
import com.example.financetracker.presentation.screens.home.states.EditBudgetScreenStates
import com.example.financetracker.presentation.ui.theme.FinanceTrackerTheme
import com.example.financetracker.presentation.viewmodels.EditBudgetViewModel
import com.example.financetracker.presentation.widgets.CategoryIconCompose
import java.util.Calendar

@Composable
fun EditBudgetScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: EditBudgetViewModel = hiltViewModel(),
    budgetId: Int?,
) {

    val context = LocalContext.current
    val errorMessage by remember { viewModel.errorMessage }
    val showErrorToast by remember { viewModel.showToastError }
    val isEditMode by remember { viewModel.isEditMode }
    val screenStates by remember { viewModel.screenStates }
    val budgetAmountError by remember { viewModel.budgetAmountError }
    val budgetAmount by remember { viewModel.budgetAmount }
    val budgetTitle by remember { viewModel.budgetTitle }
    val categoryBudget = remember { viewModel.categoryBudgets }
    val startCalendar by remember { viewModel.startCalender }
    val endCalendar by remember { viewModel.endCalender }
    val balanceAmount by remember { viewModel.balanceAmount }

    val startDatePicker = DatePickerDialog(
        LocalContext.current,
        { _, year, month, dayOfMonth ->
            viewModel.setStartDate(dayOfMonth, month, year)
        },
        startCalendar.get(Calendar.YEAR),
        startCalendar.get(Calendar.MONTH),
        startCalendar.get(Calendar.DAY_OF_MONTH)
    )

    val endDatePicker = DatePickerDialog(
        LocalContext.current,
        { _, year, month, dayOfMonth ->
            viewModel.setEndDate(dayOfMonth, month, year)
        },
        endCalendar.get(Calendar.YEAR),
        endCalendar.get(Calendar.MONTH),
        endCalendar.get(Calendar.DAY_OF_MONTH)
    )

    LaunchedEffect(key1 = screenStates) {
        when (screenStates) {
            EditBudgetScreenStates.LOAD_ALL_CATEGORY_SUCCESS -> {
                if (budgetId != null && budgetId > 0) {
                    viewModel.loadBudgetById(budgetId)
                    return@LaunchedEffect
                }

                if (!isEditMode) {
                    viewModel.getNewBudgetInstance()
                }
            }

            EditBudgetScreenStates.SAVE_BUDGET_SUCCESS -> {
                navController.popBackStack()
            }

            else -> {
                /* no-op */
            }
        }
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.loadAllCategories()
    }

    LaunchedEffect(key1 = showErrorToast) {
        if(showErrorToast <= 0){
            return@LaunchedEffect
        }
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
    }

    if (screenStates == EditBudgetScreenStates.LOADING) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    if (screenStates == EditBudgetScreenStates.LOAD_ALL_CATEGORY_ERROR) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = errorMessage)
        }
        return
    }

    EditBudgetScreenContent(
        modifier = modifier,
        navController = navController,
        isEditMode = isEditMode,
        onSaveBudgetClick = {
            viewModel.saveBudget()
        },
        budgetName = budgetTitle,
        budgetAmount = budgetAmount,
        endDate = viewModel.getReadableEmdDate(),
        startDate = viewModel.getReadableStartDate(),
        categoryBudget = categoryBudget,
        onCategoryBudgetUpdate = { value, index ->
            viewModel.updateCategoryBudgetAmount(value, index)
        },
        onAmountUpdate = {
            viewModel.updateBudgetAmount(it)
        },
        budgetAmountError = budgetAmountError,
        onValidateCategoryAmountInput = { value, index ->
            viewModel.onValidateCategoryAmountInput(value, index)
        },
        onEndDateClicked = {
            endDatePicker.show()
        },
        onStartDateClicked = {
            startDatePicker.show()
        },
        onTitleTextUpdate = {
            viewModel.updateBudgetTitle(it)
        },
        balanceAmount = balanceAmount
    )
}

@Composable
private fun EditBudgetScreenContent(
    modifier: Modifier = Modifier,
    navController: NavController,
    isEditMode: Boolean,
    onSaveBudgetClick: () -> Unit = {},
    budgetName: String,
    onTitleTextUpdate: (String) -> Unit = {},
    startDate: String,
    onStartDateClicked: () -> Unit = {},
    endDate: String,
    onEndDateClicked: () -> Unit = {},
    budgetAmount: String,
    onAmountUpdate: (String) -> Unit = {},
    budgetAmountError: String,
    categoryBudget: List<CategoryBudget>,
    onCategoryBudgetUpdate: (String, Int) -> Unit = { value: String, index: Int -> },
    onValidateCategoryAmountInput: (String, Int) -> String = { _, _ -> "" },
    balanceAmount: Float,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back button")
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = "${if (isEditMode) "Edit" else "Add"} Budget")
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onSaveBudgetClick()
            }) {
                Icon(Icons.Default.Check, contentDescription = "Save Button")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .padding(paddingValues)
                .padding(horizontal = 12.dp, vertical = 12.dp)
        ) {
            item {
                OutlinedTextField(
                    value = budgetName,
                    onValueChange = { onTitleTextUpdate(it) },
                    label = {
                        Text(
                            text = "Budget Name",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    placeholder = {
                        Text(text = "Enter Budget Name")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                )

                OutlinedTextField(
                    value = budgetAmount,
                    onValueChange = { onAmountUpdate(it) },
                    label = {
                        Text(
                            text = "Budget Amount",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    placeholder = {
                        Text(text = "Enter Amount")
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                )

                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                    AnimatedVisibility(visible = budgetAmount.isNotBlank()) {
                        Text(text = budgetAmountError, color = Color.Red)
                    }
                }

                Text(
                    text = "Select Budget Duration: ",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Button(onClick = { onStartDateClicked() }, modifier = Modifier.weight(1f)) {
                        Text(
                            text = startDate,
                            color = Color.White,
                            modifier = Modifier.padding(vertical = 6.dp, horizontal = 12.dp)
                        )
                    }
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp)
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "To"
                        )
                        Icon(
                            Icons.Default.ArrowForward,
                            contentDescription = "To",
                        )
                    }
                    Button(onClick = { onEndDateClicked() }, modifier = Modifier.weight(1f)) {
                        Text(
                            text = endDate,
                            color = Color.White,
                            modifier = Modifier.padding(vertical = 6.dp, horizontal = 12.dp)
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Balance",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Box(modifier = Modifier.width(12.dp))
                    Box(
                        modifier = Modifier
                            .background(
                                color = Color.Gray.copy(alpha = 0.25f),
                                shape = RoundedCornerShape(6.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = balanceAmount.toString(),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
            }

            items(categoryBudget.size) { index ->
                val catBud = categoryBudget[index]

                EditCategoryBudgetWidget(
                    modifier = Modifier,
                    categoryBudget = catBud,
                    onAmountUpdate = {
                        onCategoryBudgetUpdate(it, index)
                    },
                    onValidateCategoryAmountInput = {
                        onValidateCategoryAmountInput(it, index)
                    }
                )
            }

            item {
                Box(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditCategoryBudgetWidget(
    modifier: Modifier = Modifier,
    categoryBudget: CategoryBudget,
    onAmountUpdate: (String) -> Unit,
    onValidateCategoryAmountInput: (String) -> String,
) {
    var budgetAmount by remember { mutableStateOf(categoryBudget.amount.toString()) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var isSaved by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf("") }
    val context = LocalContext.current

    fun onSaveAmount() {
        if (error.isNotBlank()) {
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        } else {
            onAmountUpdate(budgetAmount)
            keyboardController?.hide()
            focusManager.clearFocus()
            isSaved = true
        }
    }

    Box(
        modifier = modifier
            .padding(vertical = 6.dp)
            .fillMaxWidth()
            .background(
                color =
                if (isSaved)
                    Color.Gray.copy(alpha = 0.25f)
                else
                    Color.Gray.copy(alpha = 0.45f),
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CategoryIconCompose(
                category = categoryBudget.category,
                onClick = { /* no-op */ },
                isSelected = false,
                modifier = Modifier.width(80.dp)
            )
            Box(modifier = Modifier.width(12.dp))
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = categoryBudget.category.title,
                    modifier = Modifier.padding(vertical = 6.dp)
                )
                TextField(
                    value = budgetAmount,
                    onValueChange = {
                        budgetAmount = it
                        val result = onValidateCategoryAmountInput(it)
                        isSaved = it == "${categoryBudget.amount}"
                        error = result

                    },
                    leadingIcon = {
                        Text(text = "$")
                    },
                    trailingIcon = {
                        if (!isSaved) {
                            IconButton(onClick = {
                                onSaveAmount()
                            }) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = "Save Category budget Icon"
                                )
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        onSaveAmount()
                    })
                )

                AnimatedVisibility(visible = error.isNotBlank()) {
                    Text(
                        text = error,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        textAlign = TextAlign.Right,
                        fontSize = 12.sp,
                        color = Color.Red
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    val navController = rememberNavController()
    FinanceTrackerTheme(darkTheme = false) {
        EditBudgetScreenContent(
            navController = navController,
            isEditMode = true,
            budgetAmount = "$100",
            endDate = "12th July",
            startDate = "12th Aug",
            budgetName = "June Month Budget",
            categoryBudget = CategoryBudget.getDefaultList(),
            budgetAmountError = "Not a valid number.",
            balanceAmount = 1000f
        )
    }
}
