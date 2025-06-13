package com.example.showmethemoney.navigation

sealed class Screen(val route: String) {
    object Expenses : Screen("expenses")
    object Income : Screen("income")
    object Account : Screen("account")
    object Category : Screen("category")
    object Settings : Screen("settings")
}