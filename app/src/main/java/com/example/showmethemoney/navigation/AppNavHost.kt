package com.example.showmethemoney.navigation

import androidx.compose.runtime.Composable
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
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Expenses.route
    ) {
        composable(Screen.Expenses.route) { ExpensesScreen() }
        composable(Screen.Income.route) { IncomeScreen() }
        composable(Screen.Category.route) { CategoryScreen() }
        composable(Screen.Account.route) { AccountScreen() }
        composable(Screen.Settings.route) { SettingsScreen() }
        composable(Screen.History.route) { HistoryScreen(navController) }
        composable(Screen.AddExpense.route) {
            AddTransactionScreen(navController, isIncome = false)
        }
        composable(Screen.AddIncome.route) {
            AddTransactionScreen(navController, isIncome = true)
        }
    }
}