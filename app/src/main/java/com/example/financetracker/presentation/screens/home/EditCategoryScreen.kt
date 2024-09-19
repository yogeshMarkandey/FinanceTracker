package com.example.financetracker.presentation.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.financetracker.data.models.local.PaymentType
import com.example.financetracker.domain.model.local.Category
import com.example.financetracker.presentation.ui.theme.FinanceTrackerTheme
import com.example.financetracker.presentation.viewmodels.AppViewModel
import java.util.Date

@Composable
fun EditCategoryScreen(
    modifier: Modifier = Modifier,
    appViewModel: AppViewModel,
    navController: NavController,
) {
    EditCategoryScreenContent(
        modifier = modifier, navController = navController,
        onSaveCategory = {
            appViewModel.updateCategory(category = it)
            navController.popBackStack()
        },
    )
}

@Composable
private fun EditCategoryScreenContent(
    modifier: Modifier = Modifier,
    navController: NavController,
    onSaveCategory: (Category) -> Unit
) {
    var categoryName by remember { mutableStateOf("") }

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
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                value = categoryName,
                label = { Text("Enter Category Name") },
                placeholder = { Text("Type something...") },
                leadingIcon = {
                    Icon(Icons.Default.Edit, contentDescription = "Category Name")
                },
                onValueChange = {
                    categoryName = it
                },
            )

            Button(
                modifier = Modifier,
                onClick = {
                    val category = Category(
                        id = System.currentTimeMillis().toInt(),
                        title = categoryName,
                        type = PaymentType.Expense,
                        notes = "",
                        createdOn = Date(),
                        icon = "",
                        color = "",
                        updatedOn = Date(),
                    )
                    onSaveCategory(category)
                },
            ) {
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "Save")
                Spacer(modifier = Modifier.width(6.dp))
                Icon(Icons.Default.Check, contentDescription = "Select Date")
                Spacer(modifier = Modifier.width(16.dp))
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    val navController = rememberNavController()
    FinanceTrackerTheme(darkTheme = false, dynamicColor = true) {
        EditCategoryScreenContent(navController = navController, onSaveCategory = {})
    }
}