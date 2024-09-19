package com.example.financetracker.presentation.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.financetracker.data.dataprovider.local.TransactionDatabase
import com.example.financetracker.presentation.screens.main.MainScreen
import com.example.financetracker.presentation.ui.theme.FinanceTrackerTheme
import com.example.financetracker.presentation.viewmodels.AppViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: AppViewModel by viewModels()

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
}
