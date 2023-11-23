package com.example.financetracker.presentation.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.financetracker.R
import com.example.financetracker.data.models.local.TransactionModel
import com.example.financetracker.data.repository.TransactionRepository
import com.example.financetracker.presentation.ui.theme.FinanceTrackerTheme
import com.example.financetracker.presentation.widgets.TransactionCard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    private val TAG = this.javaClass.name

    private val txnState = mutableStateOf(emptyList<TransactionModel>())
    private val isLoading = mutableStateOf(false)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isLoading.value = true
        setContent {
            FinanceTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                       AnimatedVisibility(visible = isLoading.value){
                           Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                               CircularProgressIndicator(
                                   color = Color.Black,
                                   modifier = Modifier.size(40.dp)
                               )
                           }
                       }

                        AnimatedVisibility(visible = !isLoading.value){
                            LazyColumn {
                                items(txnState.value.size) { index ->
                                    val c = txnState.value[index]
                                    TransactionCard(
                                        data = c,
                                        modifier = Modifier.fillMaxWidth()
                                            .padding(vertical = 8.dp, horizontal = 8.dp)
                                    )
                                }

                            }
                        }
                    }
                }
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            txnState.value = readExcelFile()
        }
    }


    private fun readExcelFile(): List<TransactionModel> {

        val inputStream = resources.openRawResource(R.raw.feb_2023_2)

        val repo = TransactionRepository()
        isLoading.value = false
        return repo.getTransactionFromExcelFile(inputStream)
    }
}
