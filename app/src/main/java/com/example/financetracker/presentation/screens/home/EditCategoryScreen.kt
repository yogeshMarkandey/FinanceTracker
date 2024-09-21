package com.example.financetracker.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.financetracker.domain.model.local.StandardColor
import com.example.financetracker.domain.model.local.StandardColor.Companion.toColor
import com.example.financetracker.presentation.ui.theme.FinanceTrackerTheme
import com.example.financetracker.presentation.viewmodels.EditCategoryViewModel
import com.example.financetracker.presentation.widgets.ColorsSelectionSheet
import com.example.financetracker.presentation.widgets.IconSelectionSheet
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
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
    val isEditMode by remember { viewModel.isEditMode }
    val notesText by remember { viewModel.notesText }
    val selectedColor by remember { viewModel.selectedColor }
    val availableColor by remember { viewModel.availableColors }

    val availableIcons by remember { viewModel.availableIcons }
    val selectedIcon by remember { viewModel.selectedIcon }

    val coroutineScope = rememberCoroutineScope()
    val colorBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val iconBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    LaunchedEffect(key1 = categoryId) {
        viewModel.initViewModel()
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

    ModalBottomSheetLayout(
        sheetState = iconBottomSheetState,
        sheetContent = {
            IconSelectionSheet(
                availableIcons = availableIcons,
                selectedIcon = selectedIcon,
                onSelectIcon = {
                    coroutineScope.launch {
                        viewModel.updateSelectedIcon(it)
                        iconBottomSheetState.hide()
                    }
                },
                selectedColor = selectedColor.toColor()
            )
        }) {
        ModalBottomSheetLayout(
            sheetState = colorBottomSheetState,
            sheetContent = {
                ColorsSelectionSheet(
                    modifier = Modifier,
                    availableColors = availableColor,
                    selectedColor = selectedColor,
                    onSelectColor = {
                        viewModel.updateSelectedColor(it)
                        coroutineScope.launch {
                            colorBottomSheetState.hide()
                        }
                    },
                )
            },
        ) {
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
                isEditMode = isEditMode,
                notesText = notesText,
                onNotesUpdated = {
                    viewModel.updateNotesText(it)
                },
                onEditColor = {
                    coroutineScope.launch {
                        colorBottomSheetState.show()
                    }
                },
                selectedColor = selectedColor,
                selectedIcon = selectedIcon,
                onIconSelectClick = {
                    coroutineScope.launch {
                        iconBottomSheetState.show()
                    }
                }
            )
        }
    }
}

@Composable
private fun EditCategoryScreenContent(
    modifier: Modifier = Modifier,
    navController: NavController,
    onSaveCategory: () -> Unit,
    onTitleUpdate: (String) -> Unit,
    categoryTitle: String,
    isEditMode: Boolean,
    notesText: String,
    onNotesUpdated: (String) -> Unit,
    onEditColor: () -> Unit,
    selectedColor: StandardColor,
    selectedIcon: ImageVector,
    onIconSelectClick: () -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Row {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back button")
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = "${if (isEditMode) "Edit" else "Add"} Category")
                },
            )
        },
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
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

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                value = notesText,
                onValueChange = { onNotesUpdated(it) },
                label = { Text("Notes") },
                placeholder = { Text("Type here...") },
                leadingIcon = {
                    Icon(Icons.Default.Notifications, contentDescription = "Icon Money")
                }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 12.dp)
                        .width(48.dp)
                        .clickable {
                            onEditColor()
                        }
                        .aspectRatio(1f)
                        .background(color = selectedColor.toColor(), shape = CircleShape),
                )

                Box(
                    modifier = Modifier.background(
                        color = Color.Gray.copy(alpha = .35f),
                        CircleShape
                    )
                ) {
                    IconButton(
                        onClick = { onIconSelectClick() }) {
                        Icon(
                            selectedIcon,
                            contentDescription = "Selected Icon",
                            tint = selectedColor.toColor(),
                        )
                    }
                }
            }

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
            categoryTitle = "Category Title",
            isEditMode = true,
            notesText = "Notes",
            onNotesUpdated = {},
            onEditColor = {},
            selectedColor = StandardColor.red(),
            selectedIcon = Icons.Default.Edit,
            onIconSelectClick = {}
        )
    }
}