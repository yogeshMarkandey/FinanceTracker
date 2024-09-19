package com.example.financetracker.presentation.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.financetracker.presentation.ui.theme.FinanceTrackerTheme
import com.example.financetracker.presentation.viewmodels.EditCategoryViewModel

@Composable
fun EditCategoryScreen(
    modifier: Modifier = Modifier,
    viewModel: EditCategoryViewModel = hiltViewModel(),
    navController: NavController,
    categoryId: Int? = null
) {
    val loading by remember { viewModel.isLoading }
    val error by remember { viewModel.error }
    val categoryTitle by remember { viewModel.categoryTitle }

    LaunchedEffect(key1 = categoryId) {
        if (categoryId != null && categoryId > 0) {
            viewModel.getCategoryById(categoryId)
        }
    }

    if (loading) {

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }

        return
    }

    if (error.isNotBlank()) {

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = error, color = Color.Red)
        }

        return
    }

    EditCategoryScreenContent(
        modifier = modifier, navController = navController,
        onSaveCategory = {
            viewModel.addOrEditCategory()
            navController.popBackStack()
        },
        onTitleUpdate = { value ->
            viewModel.updateCategoryTitle(value)
        },
        categoryTitle = categoryTitle,
    )
}

@Composable
private fun EditCategoryScreenContent(
    modifier: Modifier = Modifier,
    navController: NavController,
    onSaveCategory: () -> Unit,
    onTitleUpdate: (String) -> Unit,
    categoryTitle: String
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
                value = categoryTitle,
                label = { Text("Enter Category Name") },
                placeholder = { Text("Type something...") },
                leadingIcon = {
                    Icon(Icons.Default.Edit, contentDescription = "Category Name")
                },
                onValueChange = {
                    onTitleUpdate(it)
                },
            )

            Button(
                modifier = Modifier,
                onClick = {
                    onSaveCategory()
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
        EditCategoryScreenContent(
            navController = navController,
            onSaveCategory = {},
            onTitleUpdate = {},
            categoryTitle = "Category Title"
        )
    }
}