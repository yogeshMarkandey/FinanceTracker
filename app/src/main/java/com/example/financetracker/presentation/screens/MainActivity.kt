package com.example.financetracker.presentation.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.financetracker.presentation.screens.main.MainScreen
import com.example.financetracker.presentation.ui.theme.FinanceTrackerTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FinanceTrackerTheme(
                darkTheme = true
            ) {
                MainScreen()
            }
        }
    }
}
