package com.example.financetracker.presentation.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.financetracker.domain.model.local.Transaction
import com.example.financetracker.presentation.ui.theme.FinanceTrackerTheme
import com.example.financetracker.presentation.viewmodels.HomeScreenViewModel
import com.example.financetracker.presentation.widgets.CategoryIconCompose

@Composable
fun HomeScreen(
    modifier: Modifier,
    viewModel: HomeScreenViewModel = hiltViewModel(),
) {
    val isLoading = remember {
        viewModel.isLoading
    }

    val latestTransaction by remember { viewModel.latestTransaction }

    LaunchedEffect(key1 = Unit) {
        viewModel.loadLatestTransaction()
    }

    HomeScreenContent(
        modifier = modifier,
        showLoader = isLoading.value, latestTransaction = latestTransaction
    )

}

@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    showLoader: Boolean,
    latestTransaction: List<Transaction>,
) {
    val scrollState = rememberLazyListState()
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(visible = showLoader) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color.Black,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            AnimatedVisibility(visible = !showLoader) {
                LazyColumn(
                    state = scrollState
                ) {
                    item {
                        Text(
                            text = "Latest Transactions",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 12.dp, horizontal = 12.dp)
                        )
                    }
                    items(latestTransaction.size) { index ->
                        val transaction = latestTransaction[index]
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                .fillMaxWidth()
                                .background(
                                    color = Color.LightGray.copy(alpha = 0.25f),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CategoryIconCompose(
                                modifier = Modifier.width(80.dp),
                                category = transaction.category,
                                isSelected = false,
                                onClick = {},
                            )
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Rs ${transaction.amount} /-",
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = transaction.getReadableDateTime(),
                                        fontWeight = FontWeight.Light,
                                        fontSize = 12.sp
                                    )
                                }
                                Box(modifier = Modifier.height(12.dp))
                                Text(text = transaction.notes, fontWeight = FontWeight.Normal)
                            }
                        }
                    }
                }
            }
        }
    }
}


@Preview
@Composable
private fun PreviewHomeScreen() {
    FinanceTrackerTheme {
        HomeScreenContent(showLoader = false, latestTransaction = Transaction.defaultList())
    }
}
