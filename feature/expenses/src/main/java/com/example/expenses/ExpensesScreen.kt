package com.example.expenses

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ui.TransactionList
import com.example.utils.TopBarState

@Composable
fun ExpensesScreen(
    viewModelFactory: ViewModelProvider.Factory,
    navController: NavController,
    updateTopBar: (TopBarState) -> Unit
) {
    val viewModel: ExpensesViewModel = viewModel(factory = viewModelFactory)

    LaunchedEffect(Unit) {
        viewModel.loadExpenses()
    }

    LaunchedEffect(Unit) {
        updateTopBar(
            TopBarState(
                title = "Расходы сегодня",
                onActionClick = { navController.navigate("expense_history") }
            )
        )
    }

    val transactionState by viewModel.expenses.collectAsState()

    TransactionList(
        transactions = transactionState,
        onElementClick = { item ->
            navController.navigate("edit_expense?transactionId=${item.id}")
        }
    )
}