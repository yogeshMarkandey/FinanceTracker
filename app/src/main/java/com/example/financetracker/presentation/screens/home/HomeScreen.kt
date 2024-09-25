package com.example.financetracker.presentation.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.financetracker.domain.model.local.Budget
import com.example.financetracker.domain.model.local.Transaction
import com.example.financetracker.presentation.screens.navigation.Routes
import com.example.financetracker.presentation.ui.theme.FinanceTrackerTheme
import com.example.financetracker.presentation.viewmodels.HomeScreenViewModel
import com.example.financetracker.presentation.widgets.budget.ActiveBudgetItemContent
import com.example.financetracker.presentation.widgets.transaction.TransactionDetailsWidget
import com.example.financetracker.presentation.widgets.transaction.TransactionSummaryContent

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel = hiltViewModel(),
    navController: NavController,
) {
    val isLoading = remember {
        viewModel.isLoading
    }

    val balance by remember { viewModel.balanceAmount }
    val spendingAmount by remember { viewModel.spendingAmount }
    val incomeAmount by remember { viewModel.incomeAmount }
    val activeBudgetsThisMonth by remember { viewModel.activeBudgets }

    val latestTransaction by remember { viewModel.latestTransaction }

    LaunchedEffect(key1 = Unit) {
        viewModel.loadLatestTransaction()
        viewModel.loadBudgetByStartDate()
    }

    HomeScreenContent(
        modifier = modifier,
        showLoader = isLoading.value,
        latestTransaction = latestTransaction,
        incomeAmount = incomeAmount,
        balanceAmount = balance,
        spendingAmount = spendingAmount,
        onTransactionClicked = {
            navController.navigate(Routes.routeEditTransaction(it.id))
        },
        onBudgetClicked = { id ->
            navController.navigate(Routes.routeEditBudget(id))
        },
        activeBudgetsThisMonth = activeBudgetsThisMonth,
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
    onTransactionClicked: (Transaction) -> Unit,
    onBudgetClicked: (id: Int?) -> Unit = {},
    activeBudgetsThisMonth: List<Budget>,
) {
    val scrollState = rememberLazyListState()
    val transactionLength = if (latestTransaction.size < 5) latestTransaction.size else 5
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
                        TransactionSummaryContent(
                            modifier = Modifier,
                            incomeAmount = incomeAmount,
                            spendingAmount = spendingAmount,
                            balanceAmount = balanceAmount,
                        )
                    }

                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .padding(horizontal = 12.dp, vertical = 12.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "Latest Transactions",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 12.dp, horizontal = 12.dp)
                            )

                            Text(
                                text = "See more",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    items(transactionLength) { index ->
                        val transaction = latestTransaction[index]
                        TransactionDetailsWidget(
                            transaction = transaction,
                            onTransactionClicked = {
                                onTransactionClicked(transaction)
                            },
                        )
                    }

                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .padding(horizontal = 12.dp, vertical = 12.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "Active Budgets",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 12.dp, horizontal = 12.dp)
                            )

                            IconButton(onClick = { onBudgetClicked(null) }) {
                                Icon(Icons.Default.Add, contentDescription = "Add Budget Icon")
                            }
                        }
                    }

                    items(activeBudgetsThisMonth.size) { index ->
                        val budget = activeBudgetsThisMonth[index]

                        ActiveBudgetItemContent(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            budget = budget,
                            onBudgetClicked = {
                                onBudgetClicked(budget.id)
                            },
                        )
                    }

                    item {
                        Box(modifier = Modifier.height(100.dp))
                    }
                }
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
            onTransactionClicked = {},
            activeBudgetsThisMonth = listOf(Budget.getDefault())
        )
    }
}
