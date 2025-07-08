package com.example.showmethemoney.navigation

/**
 * Класс, представляющий экраны приложения.
 *
 * Содержит информацию о маршрутах (route) и заголовках (title) для каждого экрана.
 * Реализован как sealed class для ограниченного набора возможных экранов.
 *
 * @property route Уникальный идентификатор маршрута (используется в навигации)
 */
sealed class Screen(
    val route: String
) {
    object Expenses : Screen("expenses")
    object Income : Screen("income")
    object Account : Screen("account")
    object Category : Screen("category")
    object Settings : Screen("settings")
    object History : Screen("history")
    object AddExpense : Screen("add_expense")
    object AddIncome : Screen("add_income")
    object EditAccount : Screen("edit_account")
}