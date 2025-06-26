package com.example.showmethemoney.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.showmethemoney.ui.screens.sections.AccountScreen
import com.example.showmethemoney.ui.screens.sections.CategoryScreen
import com.example.showmethemoney.ui.screens.sections.HistoryScreen
import com.example.showmethemoney.ui.screens.sections.SettingsScreen
import com.example.showmethemoney.ui.screens.sections.TransactionScreen
import com.example.showmethemoney.ui.screens.sections.subsections.AddTransactionScreen

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
fun AppNavHost(navController: NavHostController,
               viewModelFactory: ViewModelProvider.Factory) {

    val context = LocalContext.current
    NavHost(
        navController = navController,
        startDestination = Screen.Expenses.route
    ) {
        composable(Screen.Expenses.route) { TransactionScreen(viewModelFactory,
            false) }
        composable(Screen.Income.route) { TransactionScreen(viewModelFactory,
            true) }
        composable(Screen.Category.route) { CategoryScreen(viewModelFactory) }
        composable(Screen.Account.route) { AccountScreen(viewModelFactory) }
        composable(Screen.Settings.route) { SettingsScreen() }
        composable(Screen.History.route) { HistoryScreen(navController, viewModelFactory) }
        composable(Screen.AddExpense.route) {
            AddTransactionScreen( isIncome = false, viewModelFactory = viewModelFactory)
        }
        composable(Screen.AddIncome.route) {
            AddTransactionScreen(isIncome = true, viewModelFactory = viewModelFactory)
        }
    }
}