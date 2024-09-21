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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.style.TextAlign
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

    val balance by remember { viewModel.balanceAmount }
    val spendingAmount by remember { viewModel.spendingAmount }
    val incomeAmount by remember { viewModel.incomeAmount }

    val latestTransaction by remember { viewModel.latestTransaction }

    LaunchedEffect(key1 = Unit) {
        viewModel.loadLatestTransaction()
    }

    HomeScreenContent(
        modifier = modifier,
        showLoader = isLoading.value,
        latestTransaction = latestTransaction,
        incomeAmount = incomeAmount,
        balanceAmount = balance,
        spendingAmount = spendingAmount
    )
}

@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    showLoader: Boolean,
    latestTransaction: List<Transaction>,
    incomeAmount: Float,
    balanceAmount: Float,
    spendingAmount: Float,
) {
    val scrollState = rememberLazyListState()
    Surface(
        modifier = modifier.fillMaxSize(),
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
                        ThisMonthTransactionDetailsContent(
                            modifier = Modifier,
                            incomeAmount = incomeAmount,
                            spendingAmount = spendingAmount,
                            balanceAmount = balanceAmount,
                        )
                    }

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

@Composable
private fun ThisMonthTransactionDetailsContent(
    modifier: Modifier = Modifier,
    spendingAmount: Float,
    incomeAmount: Float,
    balanceAmount: Float,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "This month",
            modifier = Modifier
                .align(Alignment.Start)
                .padding(horizontal = 12.dp, vertical = 12.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        ) {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .weight(1f)
                    .background(
                        color = Color.Green.copy(alpha = .25f),
                        shape = CircleShape
                    )
                    .padding(vertical = 12.dp, horizontal = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    Text(
                        text = "$$incomeAmount",
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 20.sp
                    )
                    Text(
                        text = "Income",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 14.sp
                    )
                }
            }
            Box(modifier = Modifier.width(12.dp))
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .weight(1f)
                    .background(
                        color = Color.Red.copy(alpha = .25f),
                        shape = CircleShape
                    )
                    .padding(vertical = 12.dp, horizontal = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    Text(
                        text = "$$spendingAmount",
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 20.sp
                    )
                    Text(
                        text = "Expense",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 14.sp
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .padding(vertical = 4.dp)
                .background(
                    color = Color.Black.copy(alpha = .2f),
                    shape = CircleShape
                )
                .padding(vertical = 8.dp, horizontal = 25.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(modifier = Modifier.wrapContentSize()) {
                Text(
                    text = "$$balanceAmount",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp
                )
                Text(
                    text = "Balance",
                    textAlign = TextAlign.Center,
                    fontSize = 10.sp
                )
            }
        }

    }
}


@Preview
@Composable
private fun PreviewHomeScreen() {
    FinanceTrackerTheme(darkTheme = true) {
        HomeScreenContent(
            showLoader = false,
            latestTransaction = Transaction.defaultList(),
            balanceAmount = 1220f,
            spendingAmount = 12200f,
            incomeAmount = 122000f,
        )
    }
}
