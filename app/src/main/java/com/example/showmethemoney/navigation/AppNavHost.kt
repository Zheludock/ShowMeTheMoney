package com.example.showmethemoney.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.showmethemoney.ui.screens.sections.AccountScreen
import com.example.showmethemoney.ui.screens.sections.CategoryScreen
import com.example.showmethemoney.ui.screens.sections.ExpensesScreen
import com.example.showmethemoney.ui.screens.sections.IncomeScreen
import com.example.showmethemoney.ui.screens.sections.SettingsScreen
import com.example.showmethemoney.ui.screens.sections.subsections.HistoryScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Expenses.route
    ) {
        composable(Screen.Expenses.route) { ExpensesScreen()}
        composable(Screen.Income.route) { IncomeScreen(viewModel()) }
        composable(Screen.Category.route) { CategoryScreen(viewModel()) }
        composable(Screen.Account.route) { AccountScreen() }
        composable(Screen.Settings.route) { SettingsScreen() }
        composable(Screen.History.route) { HistoryScreen(navController = navController) }
    }
}