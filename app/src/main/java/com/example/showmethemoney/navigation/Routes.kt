package com.example.showmethemoney.navigation

import androidx.annotation.StringRes
import com.example.showmethemoney.R

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
    @StringRes val title: Int
) {
    object Expenses : Screen("expenses", R.string.expenses_today)
    object Income : Screen("income", R.string.incomes_today)
    object Account : Screen("account", R.string.my_account)
    object Category : Screen("category", R.string.my_categories)
    object Settings : Screen("settings", R.string.settings)
    object History : Screen("history", R.string.my_history)
    object AddExpense : Screen("add_expense", R.string.add_expense)
    object AddIncome : Screen("add_income", R.string.add_income)

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
                else -> Expenses
            }
        }
    }
}