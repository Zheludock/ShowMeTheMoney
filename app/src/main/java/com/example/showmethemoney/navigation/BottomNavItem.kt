package com.example.showmethemoney.navigation

import androidx.annotation.DrawableRes
import com.example.showmethemoney.R

data class BottomNavItem(
    val route: String,
    @DrawableRes val icon: Int,
    val label: String,
    val showFab: Boolean = false,
    val title: String
)

object BottomNavItems {
    val items = listOf(
        BottomNavItem(
            route = Screen.Expenses.route,
            icon = R.drawable.ic_expenses,
            label = "Расходы",
            title = "Расходы сегодня",
            showFab = true
        ),
        BottomNavItem(
            route = Screen.Income.route,
            icon = R.drawable.ic_income,
            label = "Доходы",
            title = "Доходы сегодня",
            showFab = true
        ),
        BottomNavItem(
            route = Screen.Account.route,
            icon = R.drawable.ic_account,
            label = "Счет",
            title = "Мой счет",
            showFab = true
        ),
        BottomNavItem(
            route = Screen.Category.route,
            icon = R.drawable.ic_article,
            label = "Статьи",
            title = "Мои статьи"
        ),
        BottomNavItem(
            route = Screen.Settings.route,
            icon = R.drawable.ic_settings,
            label = "Настройки",
            title = "Настройки"
        ),
    )
}