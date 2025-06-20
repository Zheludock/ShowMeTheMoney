package com.example.showmethemoney.navigation

sealed class Screen(val route: String,
    val title: String) {
    object Expenses : Screen(
        "expenses",
        "Расходы сегодня"
    )
    object Income : Screen(
        "income",
        "Доходы сегодня"
    )
    object Account : Screen(
        "account",
        "Мой счет"
    )
    object Category : Screen(
        "category",
        "Мои статьи"
    )
    object Settings : Screen(
        "settings",
        "Настройки"
    )
    object History: Screen(
        "history",
        "Моя история"
    )
    object AddExpense: Screen(
        "add_expense",
        "Мои расходы"
    )
    object AddIncome: Screen(
        "add_income",
        "Мои доходы"
    )

    companion object {
        fun fromRoute(route: String?): Screen {
            return when (route) {
                Expenses.route -> Expenses
                Income.route -> Income
                Account.route -> Account
                Category.route -> Category
                Settings.route -> Settings
                History.route -> History
                else -> Expenses
            }
        }
    }
}