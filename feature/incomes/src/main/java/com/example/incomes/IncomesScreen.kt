package com.example.incomes

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
fun IncomesScreen(
    viewModelFactory: ViewModelProvider.Factory,
    navController: NavController,
    updateTopBar: (TopBarState) -> Unit
) {
    val viewModel: IncomesViewModel = viewModel(factory = viewModelFactory)

    LaunchedEffect(Unit) {
        viewModel.loadIncomes()
    }

    LaunchedEffect(Unit) {
        updateTopBar(
            TopBarState(
                title = "Доходы сегодня",
                onActionClick = { navController.navigate("income_history") }
            )
        )
    }

    val transactionState by viewModel.incomes.collectAsState()

    TransactionList(
        transactions = transactionState,
        onElementClick = { item ->
            navController.navigate("edit_income?transactionId=${item.id}")
        }
    )
}