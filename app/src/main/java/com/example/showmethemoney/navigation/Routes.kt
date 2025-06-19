package com.example.showmethemoney.navigation

sealed class Screen(val route: String,
    val title: String) {
    object Expenses : Screen(
        "expenses",
            title = "Расходы сегодня"
    )
    object Income : Screen(
        "income",
        title = "Доходы сегодня"
    )
    object Account : Screen(
        "account",
        title = "Мой счет"
    )
    object Category : Screen(
        "category",
        title = "Мои статьи"
    )
    object Settings : Screen(
        "settings",
        title = "Настройки"
    )
    object History: Screen(
        "history",
        "Моя история"
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