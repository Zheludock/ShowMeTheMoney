package com.example.showmethemoney.ui.screens.sections.subsections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.showmethemoney.R
import com.example.showmethemoney.data.safecaller.ApiResult
import com.example.showmethemoney.navigation.Screen
import com.example.showmethemoney.ui.components.ExpenseItem
import com.example.showmethemoney.ui.components.IncomeItem
import com.example.showmethemoney.ui.components.UniversalListItem
import com.example.showmethemoney.ui.screens.sections.ErrorView
import com.example.showmethemoney.ui.screens.sections.ExpensesViewModel
import com.example.showmethemoney.ui.screens.sections.LoadingIndicator
import com.example.showmethemoney.ui.theme.Indicator

@Composable
fun HistoryScreen(
    navController: NavController,
    viewModel: ExpensesViewModel = hiltViewModel(),
) {
    val isIncome = when (navController.previousBackStackEntry?.destination?.route) {
        Screen.Expenses.route -> false
        Screen.Income.route -> true
        else -> false
    }

    LaunchedEffect(isIncome) {
        viewModel.loadTransactions(isIncome = isIncome)
    }

    if (isIncome) {
        val incomesState by viewModel.incomes.collectAsState()
        when (val state = incomesState) {
            is ApiResult.Loading -> LoadingIndicator()
            is ApiResult.Success -> IncomeHistoryList(
                incomes = state.data
            )
            is ApiResult.Error -> ErrorView(
                error = state.error,
                onRetry = { viewModel.loadTransactions(isIncome = true) }
            )
        }
    } else {
        val expensesState by viewModel.expenses.collectAsState()
        when (val state = expensesState) {
            is ApiResult.Loading -> LoadingIndicator()
            is ApiResult.Success -> ExpenseHistoryList(
                expenses = state.data
            )
            is ApiResult.Error -> ErrorView(
                error = state.error,
                onRetry = { viewModel.loadTransactions(isIncome = false) }
            )
        }
    }
}

@Composable
fun ExpenseHistoryList(expenses: List<ExpenseItem>){
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        item {
            UniversalListItem(
                content = "Начало" to null,
                trail = "Февраль 2025" to null,
                modifier = Modifier
                    .background(Indicator)
                    .height(56.dp)
                    .padding(bottom = 1.dp)
            )
        }
        item {
            UniversalListItem(
                content = "Конец" to null,
                trail = "Июнь 2025" to null,
                modifier = Modifier
                    .background(Indicator)
                    .height(56.dp)
                    .padding(bottom = 1.dp)
            )
        }
        item {
            UniversalListItem(
                content = "Сумма" to null,
                trail = "500 000 ₽" to null,
                modifier = Modifier
                    .background(Indicator)
                    .height(56.dp)
                    .padding(bottom = 1.dp)
            )
        }
        items(expenses) { item ->
            UniversalListItem(
                lead = item.categoryEmoji,
                content = item.categoryName to item.comment,
                trail = ("${item.amount} ${item.accountCurrency}") to {
                    Icon(
                        painter = painterResource(R.drawable.ic_more_vert),
                        contentDescription = "Подробнее",
                        modifier = Modifier.size(24.dp)
                    )
                }
            )
        }
    }
}

@Composable
fun IncomeHistoryList(incomes: List<IncomeItem>){
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        item {
            UniversalListItem(
                content = "Начало" to null,
                trail = "Февраль 2025" to null,
                modifier = Modifier
                    .background(Indicator)
                    .height(56.dp)
                    .padding(bottom = 1.dp)
            )
        }
        item {
            UniversalListItem(
                content = "Конец" to null,
                trail = "Июнь 2025" to null,
                modifier = Modifier
                    .background(Indicator)
                    .height(56.dp)
                    .padding(bottom = 1.dp)
            )
        }
        item {
            UniversalListItem(
                content = "Сумма" to null,
                trail = "500 000 ₽" to null,
                modifier = Modifier
                    .background(Indicator)
                    .height(56.dp)
                    .padding(bottom = 1.dp)
            )
        }
        items(incomes) { item ->
            UniversalListItem(
                content = item.categoryName to item.comment,
                trail = ("${item.amount} ${item.accountCurrency}") to {
                    Icon(
                        painter = painterResource(R.drawable.ic_more_vert),
                        contentDescription = "Подробнее",
                        modifier = Modifier.size(24.dp)
                    )
                }
            )
        }
    }
}