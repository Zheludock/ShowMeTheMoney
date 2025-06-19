package com.example.showmethemoney.navigation

import androidx.annotation.DrawableRes
import com.example.showmethemoney.R

data class BottomNavItem(
    val route: String,
    @DrawableRes val icon: Int,
    val label: String,
    val showFab: Boolean = false
)

object BottomNavItems {
    val items = listOf(
        BottomNavItem(
            route = Screen.Expenses.route,
            icon = R.drawable.ic_expenses,
            label = "Расходы",
            showFab = true
        ),
        BottomNavItem(
            route = Screen.Income.route,
            icon = R.drawable.ic_income,
            label = "Доходы",
            showFab = true
        ),
        BottomNavItem(
            route = Screen.Account.route,
            icon = R.drawable.ic_account,
            label = "Счет",
            showFab = true
        ),
        BottomNavItem(
            route = Screen.Category.route,
            icon = R.drawable.ic_article,
            label = "Статьи"
        ),
        BottomNavItem(
            route = Screen.Settings.route,
            icon = R.drawable.ic_settings,
            label = "Настройки"
        ),
    )
}