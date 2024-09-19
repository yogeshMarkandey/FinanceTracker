package com.example.financetracker.presentation.screens.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.financetracker.presentation.screens.home.BottomNavigationBar
import com.example.financetracker.presentation.screens.home.EditTransactionScreen
import com.example.financetracker.presentation.screens.navigation.MainScreenBottomNavigationGraph
import com.example.financetracker.presentation.screens.navigation.Routes
import com.example.financetracker.presentation.ui.theme.FinanceTrackerTheme
import com.example.financetracker.presentation.viewmodels.AppViewModel

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: AppViewModel = hiltViewModel(),
) {
    val mainNavController = rememberNavController()
    NavHost(navController = mainNavController, startDestination = Routes.ROOT) {
        composable(Routes.EditTransactionScreen) {
            EditTransactionScreen(
                modifier = modifier,
                appViewModel = viewModel,
                navController = mainNavController,
            )
        }

        composable(route = Routes.ROOT) {
            RootScreen(viewModel = viewModel, mainNavController = mainNavController)
        }
    }
}

@Composable
fun RootScreen(
    modifier: Modifier = Modifier,
    viewModel: AppViewModel,
    mainNavController: NavController,
) {
    val bottomNavController = rememberNavController()
    Scaffold(
        modifier = modifier,
        bottomBar = { BottomNavigationBar(navController = bottomNavController) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                mainNavController.navigate(Routes.EditTransactionScreen)
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Transaction")
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true
    ) {
        MainScreenBottomNavigationGraph(
            navController = bottomNavController,
            viewModel,
            Modifier.padding(it),
        )
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    FinanceTrackerTheme(
        darkTheme = true
    ) {
        MainScreen()
    }
}