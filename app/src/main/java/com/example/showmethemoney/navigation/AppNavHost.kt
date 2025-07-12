package com.example.showmethemoney.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.account.AccountScreen
import com.example.account.editaccount.EditAccountScreen
import com.example.category.CategoryScreen
import com.example.settings.SettingsScreen
import com.example.transactions.addtransaction.AddTransactionScreen
import com.example.transactions.TransactionScreen
import com.example.transactions.transactionhistory.TransactionHistoryScreen
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
        composable(Screen.Expenses.route) {
            TransactionScreen(
                viewModelFactory,
                false, navController, updateTopBar
            )
        }
        composable(Screen.Income.route) {
            TransactionScreen(
                viewModelFactory,
                true, navController, updateTopBar
            )
        }
        composable(Screen.Category.route) { CategoryScreen(viewModelFactory, updateTopBar) }
        composable(Screen.Account.route) {
            AccountScreen(
                viewModelFactory,
                navController,
                updateTopBar
            )
        }
        composable(Screen.Settings.route) { SettingsScreen(updateTopBar) }
        composable(Screen.History.route) {
            TransactionHistoryScreen(
                navController,
                viewModelFactory,
                updateTopBar
            )
        }
        composable(Screen.EditAccount.route) {
            EditAccountScreen(
                viewModelFactory,
                navController,
                updateTopBar
            )
        }
        composable(
            route = "add_expense?transactionId={transactionId}",
            arguments = listOf(
                navArgument("transactionId") {
                    type = NavType.IntType
                    nullable = false
                    defaultValue = -1
                }
            )
        ) { backStackEntry ->
            val transactionId = backStackEntry.arguments?.getInt("transactionId") ?: -1
            val realTransactionId = if (transactionId == -1) null else transactionId
            AddTransactionScreen(viewModelFactory, navController, updateTopBar, realTransactionId)
        }
        composable(
            route = "add_income?transactionId={transactionId}",
            arguments = listOf(
                navArgument("transactionId") {
                    type = NavType.IntType
                    nullable = false
                    defaultValue = -1
                }
            )
        ) { backStackEntry ->
            val transactionId = backStackEntry.arguments?.getInt("transactionId") ?: -1
            val realTransactionId = if (transactionId == -1) null else transactionId
            AddTransactionScreen(viewModelFactory, navController, updateTopBar, realTransactionId)
        }
    }
}
