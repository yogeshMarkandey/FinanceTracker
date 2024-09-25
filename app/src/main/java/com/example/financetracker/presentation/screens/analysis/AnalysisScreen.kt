package com.example.financetracker.presentation.screens.analysis

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.financetracker.domain.model.CategoryAnalysisResult
import com.example.financetracker.domain.model.local.Budget
import com.example.financetracker.presentation.ui.theme.FinanceTrackerTheme
import com.example.financetracker.presentation.utils.DateTimeHelper
import com.example.financetracker.presentation.viewmodels.AnalysisScreenViewModel
import com.example.financetracker.presentation.widgets.budget.ActiveBudgetItemContent
import com.example.financetracker.presentation.widgets.category.CategoryAnalysisContent
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun AnalysisScreen(
    modifier: Modifier = Modifier,
    viewModel: AnalysisScreenViewModel = hiltViewModel(),
    navController: NavController,
) {
    val context = LocalContext.current
    val startCalendar by remember { viewModel.startCalender }
    val endCalendar by remember { viewModel.endCalendar }
    val selectedMode by remember { viewModel.selectedMode }
    val availableModes by remember { viewModel.availableAnalysisScreenModes }
    val errorMessage by remember { viewModel.errorMessage }
    val showToastState by remember { viewModel.showToastState }
    val screenState by remember { viewModel.screenState }
    val activeBudget = remember { viewModel.activeBudgets }
    val spendingCatAnalysisResult = remember { viewModel.categoryAnalysisResults }

    LaunchedEffect(key1 = Unit) {
        viewModel.initViewModel()
    }

    LaunchedEffect(key1 = showToastState) {
        if (showToastState > 0) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(key1 = screenState) {
        when (screenState) {
            else -> {
                /* no-op */
            }
        }
    }

    if (screenState == AnalysisScreenState.LOADING) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }

    AnalysisScreenContent(
        startCalendar = startCalendar,
        endCalendar = endCalendar,
        selectedMode = selectedMode,
        modes = availableModes,
        onSelectMode = {
            viewModel.updateSelectedAnalysisMode(it)
        },
        activeBudgets = activeBudget,
        spendingCatAnalysis = spendingCatAnalysisResult,
        onUpdateDuration = {
            viewModel.updateAnalysisDuration(it)
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AnalysisScreenContent(
    modifier: Modifier = Modifier,
    startCalendar: Calendar,
    endCalendar: Calendar,
    modes: List<AnalysisScreenModes>,
    selectedMode: AnalysisScreenModes,
    onSelectMode: (AnalysisScreenModes) -> Unit,
    activeBudgets: List<Budget>,
    spendingCatAnalysis: List<CategoryAnalysisResult>,
    onUpdateDuration: (Int) -> Unit,
) {

    Scaffold(modifier = modifier.fillMaxSize()) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.Gray.copy(alpha = .40f))
                ) {
                    Text(
                        "Analysis Screen",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 14.dp)
                    )
                }
                Box(modifier = Modifier.height(20.dp))
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                        .background(
                            color = Color.Gray.copy(alpha = 0.25f),
                            shape = RoundedCornerShape(6.dp)
                        )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        for (m in modes) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = if (selectedMode == m)
                                            Color.Gray.copy(0.45f)
                                        else Color.Transparent,
                                        shape = RoundedCornerShape(6.dp)
                                    )
                                    .clickable {
                                        onSelectMode(m)
                                    }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(text = m.name)
                            }
                        }
                    }
                }
                Box(modifier = Modifier.height(20.dp))
            }

            item {
                val dateFormat =
                    SimpleDateFormat("MMMM", Locale.ENGLISH)
                val monthName = dateFormat.format(startCalendar.time)
                Row(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .fillMaxWidth()
                        .background(
                            color = Color.Gray.copy(alpha = 0.25f),
                            shape = RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = { onUpdateDuration(-1) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back Arrow")
                    }
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = monthName,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Box(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${DateTimeHelper.format(startCalendar.time, "dd MMM, YY")} -" +
                                    " ${DateTimeHelper.format(endCalendar.time, "dd MMM, YY")}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Center
                        )
                    }
                    IconButton(onClick = { onUpdateDuration(1) }) {
                        Icon(Icons.Default.ArrowForward, contentDescription = "Forward Arrow")
                    }
                }
                Box(modifier = Modifier.height(20.dp))
            }


            item {
                Text(
                    text = "Active budgets",
                    modifier = Modifier.padding(horizontal = 12.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                )
                Box(modifier = Modifier.height(20.dp))
                AnimatedVisibility(visible = activeBudgets.isEmpty()) {
                    Column {
                        Text(
                            text = "No active Budget available",
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp)
                        )
                        Box(modifier = Modifier.height(20.dp))
                    }
                }
            }

            items(activeBudgets.size) {
                val bud = activeBudgets[it]

                ActiveBudgetItemContent(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    budget = bud,
                    onBudgetClicked = { /*TODO*/ }
                )
            }

            item {
                Box(modifier = Modifier.height(20.dp))
                Text(
                    text = "Category Spending",
                    modifier = Modifier.padding(horizontal = 12.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                )
                Box(modifier = Modifier.height(20.dp))
                AnimatedVisibility(visible = spendingCatAnalysis.isEmpty()) {
                    Column {
                        Text(
                            text = "No active Budget available",
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp)
                        )
                        Box(modifier = Modifier.height(20.dp))
                    }
                }
            }

            items(spendingCatAnalysis.size) {
                val catAn = spendingCatAnalysis[it]

                CategoryAnalysisContent(catAn = catAn)
            }

            item {
                Box(modifier = Modifier.height(20.dp))
            }
        }
    }
}


@Preview
@Composable
private fun Preview() {
    FinanceTrackerTheme {
        AnalysisScreenContent(
            startCalendar = Calendar.getInstance(),
            endCalendar = Calendar.getInstance(),
            selectedMode = AnalysisScreenModes.Month,
            modes = AnalysisScreenModes.entries.toList(),
            onSelectMode = {},
            activeBudgets = listOf(Budget.getDefault()),
            spendingCatAnalysis = CategoryAnalysisResult.getDefaultList(),
            onUpdateDuration = {}
        )
    }
}