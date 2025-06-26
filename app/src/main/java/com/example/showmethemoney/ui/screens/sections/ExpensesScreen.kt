package com.example.showmethemoney.ui.screens.sections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.domain.ApiResult
import com.example.showmethemoney.ui.components.ErrorView
import com.example.showmethemoney.ui.components.LoadingIndicator
import com.example.showmethemoney.ui.components.TransactionList


@Composable
fun ExpensesScreen(viewModel: ExpensesViewModel) {
    LaunchedEffect(Unit) {
        viewModel.updateStartDate(viewModel.currentDate)
        viewModel.updateEndDate(viewModel.currentDate)
        viewModel.loadTransactions(isIncome = false)
    }
    val expensesState by viewModel.expenses.collectAsState()

    when (val state = expensesState) {
        is ApiResult.Loading -> LoadingIndicator()
        is ApiResult.Success-> TransactionList(state.data)
        is ApiResult.Error -> ErrorView(state.error) {
            viewModel.loadTransactions(isIncome = false)
        }
    }
}



