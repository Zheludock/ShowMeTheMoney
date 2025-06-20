package com.example.showmethemoney.ui.screens.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import com.example.showmethemoney.R
import com.example.showmethemoney.data.safecaller.ApiResult
import com.example.showmethemoney.ui.components.ErrorView
import com.example.showmethemoney.ui.components.LoadingIndicator
import com.example.showmethemoney.ui.components.UniversalListItem
import com.example.showmethemoney.ui.theme.Indicator
import com.example.showmethemoney.ui.utils.ExpenseItem
import com.example.showmethemoney.ui.utils.formatAmount


@Composable
fun ExpensesScreen(viewModel: ExpensesViewModel = hiltViewModel()) {
    LaunchedEffect(Unit) {
        viewModel.updateStartDate(viewModel.currentDate)
        viewModel.updateEndDate(viewModel.currentDate)
        viewModel.loadTransactions(isIncome = false)
    }
    val expensesState by viewModel.expenses.collectAsState()

    when (val state = expensesState) {
        is ApiResult.Loading -> LoadingIndicator()
        is ApiResult.Success-> ExpenseList(state.data)
        is ApiResult.Error -> ErrorView(state.error) {
            viewModel.loadTransactions(isIncome = false)
        }
    }
}

@Composable
fun ExpenseList(expenses: List<ExpenseItem>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        item {
            UniversalListItem(
                content = "Всего" to null,
                trail = formatAmount(expenses.sumOf { it.amount.toDoubleOrNull() ?: 0.0 },
                    expenses.firstOrNull()?.accountCurrency ?: "RUB"
                ) to null,
                modifier = Modifier
                    .background(Indicator)
                    .height(56.dp)
            )
        }
        items(expenses) { item ->
            UniversalListItem(
                lead = item.categoryEmoji,
                content = item.categoryName to item.comment,
                trail = formatAmount(item.amount.toDoubleOrNull() ?: 0.0, item.accountCurrency) to {
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

