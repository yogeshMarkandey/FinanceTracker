package com.example.financetracker.presentation.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.financetracker.data.dataprovider.local.TransactionDatabase
import com.example.financetracker.presentation.screens.home.BottomNavigationBar
import com.example.financetracker.presentation.screens.navigation.NavigationGraph
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
            MainScreen()
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
            bottomBar = { BottomNavigationBar(navController = navController) }
        ) {
            NavigationGraph(navController = navController, viewModel, Modifier.padding(it))
        }
    }
}
