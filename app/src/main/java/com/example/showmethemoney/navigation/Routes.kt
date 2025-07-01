package com.example.showmethemoney.navigation

import com.example.showmethemoney.ui.utils.AccountManager

/**
 * Класс, представляющий экраны приложения.
 *
 * Содержит информацию о маршрутах (route) и заголовках (title) для каждого экрана.
 * Реализован как sealed class для ограниченного набора возможных экранов.
 *
 * @property route Уникальный идентификатор маршрута (используется в навигации)
 * @property title Заголовок экрана, отображаемый в UI
 */
sealed class Screen(
    val route: String,
    val title: String
) {
    object Expenses : Screen("expenses", "Расходы сегодня")
    object Income : Screen("income", "Доходы сегодня")
    object Account : Screen("account", AccountManager.selectedAccountName)
    object Category : Screen("category", "Статьи")
    object Settings : Screen("settings", "Настройки")
    object History : Screen("history", "Моя история")
    object AddExpense : Screen("add_expense", "Мои расходы")
    object AddIncome : Screen("add_income", "Мои доходы")
    object EditAccount : Screen("edit_account", "Редактирование аккаунта")

    companion object {
        fun fromRoute(route: String?): Screen {
            return when (route) {
                Expenses.route -> Expenses
                Income.route -> Income
                Account.route -> Account
                Category.route -> Category
                Settings.route -> Settings
                History.route -> History
                AddExpense.route -> AddExpense
                AddIncome.route -> AddIncome
                EditAccount.route -> EditAccount
                else -> Expenses
            }
        }
    }
}