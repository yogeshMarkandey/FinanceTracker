package com.example.financetracker.presentation.screens.home

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.financetracker.data.models.local.PaymentType
import com.example.financetracker.domain.model.local.Category
import com.example.financetracker.domain.model.local.StandardColor.Companion.toColor
import com.example.financetracker.presentation.common.IconHelper
import com.example.financetracker.presentation.screens.navigation.Routes
import com.example.financetracker.presentation.ui.theme.FinanceTrackerTheme
import com.example.financetracker.presentation.viewmodels.EditTransactionViewModel
import kotlinx.coroutines.launch
import java.util.Calendar

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditTransactionScreen(
    modifier: Modifier,
    viewModel: EditTransactionViewModel = hiltViewModel(),
    navController: NavController,
) {

    val calendar by remember { viewModel.calendar }
    val selectedDate =
        "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH) + 1}/" +
                "${calendar.get(Calendar.YEAR)}"

    val selectedTime = calendar.get(Calendar.HOUR_OF_DAY).toString().padStart(2, '0') +
            ":${calendar.get(Calendar.MINUTE).toString().padStart(2, '0')} " +
            if (calendar.get(Calendar.AM_PM) == 0) "AM" else "PM"

    val amountText by remember { viewModel.amountText }
    val showAmountError by remember { viewModel.showAmountTextError }
    val notesText by remember { viewModel.noteText }
    val selectedPaymentType by remember {
        viewModel.selectedPaymentType
    }

    val allCategories by remember {
        viewModel.allCategory
    }

    val selectedCategory by remember {
        viewModel.selectedCategory
    }

    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _, year, month, dayOfMonth ->
            viewModel.selectDate(dayOfMonth, month, year)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )


    val timePickerDialog = TimePickerDialog(
        LocalContext.current,
        { _, hourOfDay, minute ->
            viewModel.selectTime(hourOfDay, minute)
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true
    )

    val categorySheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()

    var categoryBottomSheetEditMode by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        viewModel.initViewModel()
    }

    ModalBottomSheetLayout(
        sheetState = categorySheetState,
        sheetContent = {
            CategoryBottomSheetContent(
                allCategory = allCategories,
                selectedCategory = selectedCategory,
                onItemSelected = { category ->
                    if (categoryBottomSheetEditMode) {
                        val route = Routes.routeEditCategory(categoryId = category.id)
                        navController.navigate(
                            route = route
                        )

                        return@CategoryBottomSheetContent
                    }

                    viewModel.updateSelectedCategory(category)
                    coroutineScope.launch {
                        if (categorySheetState.isVisible) {
                            categorySheetState.hide()
                        }
                    }
                },
                onAddNewCategory = {
                    val route = Routes.routeEditCategory()
                    navController.navigate(
                        route = route
                    )
                },
                editMode = categoryBottomSheetEditMode,
                onToggleMode = {
                    categoryBottomSheetEditMode = it
                }
            )
        },
    ) {
        EditTransactionScreenContent(
            modifier = modifier,
            navController = navController,
            onClickDate = {
                datePickerDialog.show()
            },
            selectedDate = selectedDate,
            onClickTime = {
                timePickerDialog.show()
            },
            selectedTime = selectedTime,
            onCategoryIconClick = {
                coroutineScope.launch {
                    categorySheetState.show()
                }
            },
            selectedCategory = selectedCategory,
            amountText = amountText,
            notesText = notesText,
            onNotesUpdated = {
                viewModel.updateNoteText(it)
            },
            onAmountUpdated = {
                viewModel.updateAmountText(it)
            },
            selectedPaymentType = selectedPaymentType,
            onSelectPaymentType = {
                viewModel.updateSelectedPaymentType(it)
            },
            showAmountError = showAmountError,
            onSaveTransactionClick = {
                viewModel.saveTransaction()
            }
        )
    }
}

@Composable
private fun EditTransactionScreenContent(
    modifier: Modifier = Modifier,
    navController: NavController,
    onClickDate: () -> Unit,
    selectedDate: String,
    onClickTime: () -> Unit,
    selectedTime: String,
    onCategoryIconClick: () -> Unit,
    selectedCategory: Category,
    amountText: String,
    notesText: String,
    onNotesUpdated: (String) -> Unit,
    onAmountUpdated: (String) -> Unit,
    selectedPaymentType: PaymentType,
    onSelectPaymentType: (PaymentType) -> Unit,
    showAmountError: Boolean,
    onSaveTransactionClick: () -> Unit,
) {
    Scaffold(
        modifier = modifier,
        backgroundColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.primary,
        topBar = {
            TopAppBar(
                title = {
                    Row {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back button")
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = "Edit Transaction")
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onSaveTransactionClick()
            }) {
                Icon(Icons.Default.Check, contentDescription = "Save Button")
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(it)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Button(onClick = { onClickDate() }) {
                    Icon(Icons.Default.DateRange, contentDescription = "Select Date")
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = selectedDate)
                }
                Button(onClick = { onClickTime() }) {
                    Icon(Icons.Outlined.DateRange, contentDescription = "Select Time")
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = selectedTime)
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(it)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onCategoryIconClick()
                        }
                        .background(Color.Gray.copy(alpha = 0.1f))
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        IconHelper.getIconByName(selectedCategory.icon),
                        contentDescription = "Category Icon",
                        tint = selectedCategory.color.toColor()
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = selectedCategory.title,
                        color = selectedCategory.color.toColor()
                    )
                }

                TextField(
                    value = amountText,
                    onValueChange = { onAmountUpdated(it) },
                    label = { Text("Enter Amount") },
                    placeholder = { Text("Type something...") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(Icons.Default.Call, contentDescription = "Icon Money")
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                AnimatedVisibility(
                    visible = showAmountError,
                    modifier = Modifier.align(Alignment.End),
                ) {
                    Text(
                        text = "Please enter valid Amount!",
                        color = Color.Red,
                    )
                }

                // OutlinedTextField
                OutlinedTextField(
                    value = notesText,
                    onValueChange = { onNotesUpdated(it) },
                    label = { Text("Write Notes") },
                    placeholder = { Text("Type here...") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(Icons.Default.Notifications, contentDescription = "Icon Money")
                    }
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                for (v in PaymentType.entries) {
                    Box(
                        modifier = Modifier
                            .clickable {
                                onSelectPaymentType(v)
                            }
                            .background(
                                color =
                                if (selectedPaymentType == v)
                                    Color.Gray.copy(alpha = 0.40f)
                                else Color.Gray.copy(alpha = 0.10f)
                            )
                            .padding(horizontal = 16.dp, vertical = 12.dp)

                    ) {
                        Text(text = v.name, textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}


@Composable
fun CategoryBottomSheetContent(
    modifier: Modifier = Modifier,
    allCategory: List<Category>,
    selectedCategory: Category,
    onItemSelected: (Category) -> Unit,
    onAddNewCategory: () -> Unit,
    editMode: Boolean,
    onToggleMode: (Boolean) -> Unit
) {


    Surface {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Select Category")
                Row(
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    IconButton(
                        onClick = {
                            onToggleMode(!editMode)
                        },
                        modifier = Modifier.size(64.dp),

                        ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .background(if (editMode) Color.Gray else Color.White)
                                .padding(8.dp),
                        ) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Toggled On",
                                tint = if (editMode) Color.White else Color.Gray,
                            )
                        }
                    }
                    IconButton(onClick = {
                        onAddNewCategory()
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Category Button")
                    }
                }
            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(allCategory.size) { index ->
                    val item = allCategory[index]
                    CategoryGridItem(
                        category = item,
                        isSelected = item.id == selectedCategory.id,
                        onClick = {
                            onItemSelected.invoke(item)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryGridItem(category: Category, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clickable {
                onClick()
            }
            .aspectRatio(1f)
            .background(
                color = if (isSelected) Color.Gray.copy(0.40f) else Color.Gray.copy(0.10f),
                shape = CircleShape
            )
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    IconHelper.getIconByName(category.icon),
                    contentDescription = "Category Icon",
                    tint = category.color.toColor(),
                    modifier = Modifier.size(36.dp)
                )
                Box(modifier = Modifier.height(6.dp))
                Text(
                    text = category.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = category.color.toColor(),
                )
            }
        }
    }
}

@Preview
@Composable
private fun CategoryBottomSheetContentPreview() {
    val list = Category.getDefaults()
    IconHelper.init()
    FinanceTrackerTheme {
        CategoryBottomSheetContent(
            allCategory = list,
            selectedCategory = list[1],
            onItemSelected = {},
            onAddNewCategory = {},
            editMode = false,
            onToggleMode = {}
        )
    }
}

@Preview
@Composable
private fun EditScreenPreview() {
    val navController = rememberNavController()
    FinanceTrackerTheme(darkTheme = false, dynamicColor = true) {
        EditTransactionScreenContent(
            modifier = Modifier,
            navController = navController,
            onClickDate = {},
            selectedDate = "12 Jan",
            onClickTime = {},
            selectedTime = "12:35 AM",
            onCategoryIconClick = {},
            selectedCategory = Category.getDefaults()[2],
            amountText = "400",
            notesText = "Notes",
            onNotesUpdated = {},
            onAmountUpdated = {},
            selectedPaymentType = PaymentType.Expense,
            onSelectPaymentType = {},
            showAmountError = true,
            onSaveTransactionClick = {}
        )
    }
}