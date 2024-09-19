package com.example.financetracker.presentation.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.navigation.compose.rememberNavController
import com.example.financetracker.data.dataprovider.local.TransactionDatabase
import com.example.financetracker.presentation.screens.home.BottomNavigationBar
import com.example.financetracker.presentation.screens.navigation.MainScreenBottomNavigationGraph
import com.example.financetracker.presentation.screens.navigation.Routes
import com.example.financetracker.presentation.ui.theme.FinanceTrackerTheme
import com.example.financetracker.presentation.viewmodels.AppViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    private val viewModel: AppViewModel = AppViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setLoadingState(true)
        viewModel.getDataFromFile(applicationContext)
        getDatabaseInstance()
        setContent {
            FinanceTrackerTheme(
                darkTheme = false
            ) {
                MainScreen()
            }
        }
    }

    private fun getDatabaseInstance() {
        CoroutineScope(Dispatchers.IO).launch {
            val db = TransactionDatabase.getDatabase(applicationContext)
            val items = db.transactionDao().getAllTransaction()
            println("Items : ${items.size}")
        }
    }

    @Composable
    private fun MainScreen() {
        val navController = rememberNavController()
        Scaffold(
            bottomBar = { BottomNavigationBar(navController = navController) },
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    navController.navigate(Routes.EditTransactionScreen)
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Add Transaction")
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
            isFloatingActionButtonDocked = true
        ) {
            MainScreenBottomNavigationGraph(
                navController = navController,
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
}
