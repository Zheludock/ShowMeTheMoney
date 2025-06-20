package com.example.showmethemoney.ui.screens.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.showmethemoney.R
import com.example.showmethemoney.data.safecaller.ApiError
import com.example.showmethemoney.data.safecaller.ApiResult
import com.example.showmethemoney.ui.components.ExpenseItem
import com.example.showmethemoney.ui.components.UniversalListItem
import com.example.showmethemoney.ui.theme.Indicator
import com.example.showmethemoney.ui.utils.formatAmount


@Composable
fun ExpensesScreen(viewModel: ExpensesViewModel = hiltViewModel()) {
    viewModel.loadTransactions(startDate = viewModel.currentDate, isIncome = false)
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
                trail = formatAmount(expenses.sumOf { it.amount.toDoubleOrNull() ?: 0.0 }, expenses.first().accountCurrency) to null,
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

@Composable
internal fun LoadingIndicator() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
internal fun ErrorView(error: ApiError, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = when (error) {
                is ApiError.NoInternetError -> "Нет интернет-соединения"
                is ApiError.HttpError -> "Ошибка сервера: ${error.code}"
                is ApiError.NetworkError -> "Сетевая ошибка"
                is ApiError.UnknownError -> "Неизвестная ошибка"
            }
        )
    }
}

