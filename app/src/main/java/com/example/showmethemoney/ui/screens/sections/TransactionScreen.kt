package com.example.showmethemoney.ui.screens.sections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.domain.ApiResult
import com.example.showmethemoney.ui.components.ErrorView
import com.example.showmethemoney.ui.components.LoadingIndicator
import com.example.showmethemoney.ui.components.TransactionList
//Подумать о доработке
@Composable
fun TransactionScreen(viewModelFactory: ViewModelProvider.Factory, isIncome: Boolean) {

    val viewModel: ExpensesViewModel = viewModel(factory = viewModelFactory)

    LaunchedEffect(Unit) {
        viewModel.updateStartDate(viewModel.currentDate)
        viewModel.updateEndDate(viewModel.currentDate)
        viewModel.loadTransactions(isIncome)
    }

    val transactionState by if (isIncome) {
        viewModel.incomes.collectAsState()
    } else {
        viewModel.expenses.collectAsState()
    }

    when (val state = transactionState) {
        is ApiResult.Loading -> LoadingIndicator()
        is ApiResult.Success-> TransactionList(state.data)
        is ApiResult.Error -> ErrorView(state.error) {
            viewModel.loadTransactions(isIncome)
        }
    }
}



