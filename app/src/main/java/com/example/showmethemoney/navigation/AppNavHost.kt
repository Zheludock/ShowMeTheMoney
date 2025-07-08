package com.example.showmethemoney.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.showmethemoney.ui.screens.TopBarState
import com.example.showmethemoney.ui.screens.account.AccountScreen
import com.example.showmethemoney.ui.screens.category.CategoryScreen
import com.example.showmethemoney.ui.screens.account.editaccount.EditAccountScreen
import com.example.showmethemoney.ui.screens.settings.SettingsScreen
import com.example.showmethemoney.ui.screens.transactions.transactionhistory.TransactionHistoryScreen
import com.example.showmethemoney.ui.screens.transactions.TransactionScreen

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
               viewModelFactory: ViewModelProvider.Factory,
               updateTopBar: (TopBarState) -> Unit) {
    NavHost(
        navController = navController,
        startDestination = Screen.Expenses.route
    ) {
        composable(Screen.Expenses.route) { TransactionScreen(viewModelFactory,
            false, navController, updateTopBar) }
        composable(Screen.Income.route) { TransactionScreen(viewModelFactory,
            true, navController, updateTopBar) }
        composable(Screen.Category.route) { CategoryScreen(viewModelFactory, updateTopBar) }
        composable(Screen.Account.route) { AccountScreen(viewModelFactory, navController, updateTopBar) }
        composable(Screen.Settings.route) { SettingsScreen(updateTopBar) }
        composable(Screen.History.route) { TransactionHistoryScreen(navController, viewModelFactory, updateTopBar) }
        composable(Screen.EditAccount.route) { EditAccountScreen(viewModelFactory, navController, updateTopBar) }
    }
}