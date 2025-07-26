package com.example.showmethemoney.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.account.AccountScreen
import com.example.account.editaccount.EditAccountScreen
import com.example.addexpense.AddExpenseScreen
import com.example.addincome.AddIncomeScreen
import com.example.analysis.ExpenseAnalysisScreen
import com.example.analysis.IncomeAnalysisScreen
import com.example.category.CategoryScreen
import com.example.editexpence.EditExpenseScreen
import com.example.editincome.EditIncomeScreen
import com.example.expenses.ExpensesScreen
import com.example.expenseshistory.ExpensesHistoryScreen
import com.example.incomes.IncomesScreen
import com.example.incomeshistory.IncomesHistoryScreen
import com.example.settings.ColorSelectionScreen
import com.example.settings.SettingsScreen
import com.example.utils.TopBarState

/**
 * Главный компонент навигации приложения.
 *
 * Определяет граф навигации и экраны приложения с использованием Jetpack Navigation Compose.
 *
 * @param navController Контроллер навигации для управления переходами между экранами
 * @param viewModelFactory Фабрика для создания ViewModel, используемых на экранах
 *
 * @see NavHost Базовый компонент навигации Compose
 * @see ViewModelProvider.Factory Стандартный интерфейс фабрики ViewModel
 */
@Composable
fun AppNavHost(
    navController: NavHostController,
    viewModelFactory: ViewModelProvider.Factory,
    updateTopBar: (TopBarState) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Expenses.route
    ) {
        composable(Screen.Expenses.route) { ExpensesScreen(viewModelFactory, navController, updateTopBar) }
        composable(Screen.Income.route) { IncomesScreen(viewModelFactory, navController, updateTopBar) }
        composable(Screen.Category.route) { CategoryScreen(viewModelFactory, updateTopBar) }
        composable(Screen.Account.route) { AccountScreen(viewModelFactory, navController, updateTopBar) }
        composable(Screen.Settings.route) { SettingsScreen(updateTopBar, navController) }
        composable(Screen.ExpenseHistory.route) { ExpensesHistoryScreen(navController, viewModelFactory, updateTopBar) }
        composable(Screen.IncomeHistory.route) { IncomesHistoryScreen(navController, viewModelFactory, updateTopBar) }
        composable(Screen.EditAccount.route) { EditAccountScreen(viewModelFactory, navController, updateTopBar) }
        composable(Screen.AddIncome.route) { AddIncomeScreen(viewModelFactory, navController, updateTopBar) }
        composable(Screen.AddExpense.route) { AddExpenseScreen(viewModelFactory, navController, updateTopBar) }
        composable(Screen.EditExpense.route) { EditExpenseScreen(viewModelFactory, navController, updateTopBar) }
        composable(Screen.EditIncome.route) { EditIncomeScreen(viewModelFactory, navController, updateTopBar) }
        composable(Screen.ExpenseAnalysis.route) { ExpenseAnalysisScreen(viewModelFactory, updateTopBar) }
        composable(Screen.IncomeAnalysis.route) { IncomeAnalysisScreen(viewModelFactory, updateTopBar) }
        composable (Screen.ColorSelection.route) { ColorSelectionScreen(onColorSelected = { }) }
    }
}
