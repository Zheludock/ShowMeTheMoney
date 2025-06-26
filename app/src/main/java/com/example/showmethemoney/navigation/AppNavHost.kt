package com.example.showmethemoney.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.showmethemoney.ui.screens.sections.AccountScreen
import com.example.showmethemoney.ui.screens.sections.CategoryScreen
import com.example.showmethemoney.ui.screens.sections.ExpensesScreen
import com.example.showmethemoney.ui.screens.sections.IncomeScreen
import com.example.showmethemoney.ui.screens.sections.SettingsScreen
import com.example.showmethemoney.ui.screens.sections.subsections.AddTransactionScreen
import com.example.showmethemoney.ui.screens.sections.HistoryScreen

@Composable
fun AppNavHost(navController: NavHostController,
               viewModelFactory: ViewModelProvider.Factory) {
    NavHost(
        navController = navController,
        startDestination = Screen.Expenses.route
    ) {
        composable(Screen.Expenses.route) { ExpensesScreen(viewModelFactory) }
        composable(Screen.Income.route) { IncomeScreen(viewModelFactory) }
        composable(Screen.Category.route) { CategoryScreen(viewModelFactory) }
        composable(Screen.Account.route) { AccountScreen(viewModelFactory) }
        composable(Screen.Settings.route) { SettingsScreen() }
        composable(Screen.History.route) { HistoryScreen(navController, viewModelFactory) }
        composable(Screen.AddExpense.route) {
            AddTransactionScreen( isIncome = false, viewModelFactory = viewModelFactory)
        }
        composable(Screen.AddIncome.route) {
            AddTransactionScreen(isIncome = false, viewModelFactory = viewModelFactory)
        }
    }
}