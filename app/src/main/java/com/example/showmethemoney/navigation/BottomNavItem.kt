package com.example.showmethemoney.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.showmethemoney.R

/**
 * Data-класс, представляющий элемент нижней навигационной панели.
 *
 * @property route Маршрут для навигации
 * @property icon Иконка элемента (ресурс Drawable)
 * @property label Текстовая метка элемента
 * @property showFab Флаг, указывающий нужно ли показывать FAB (Floating Action Button)
 *                  для этого элемента. По умолчанию false.
 */
data class BottomNavItem(
    val route: String,
    @DrawableRes val icon: Int,
    @StringRes val label: Int,
    val showFab: Boolean = false
)
/**
 * Объект, содержащий все элементы нижней навигационной панели.
 * Реализован как object для обеспечения singleton-доступа к данным.
 */
object BottomNavItems {
    /**
     * Список элементов навигации.
     * Порядок элементов соответствует порядку отображения в UI.
     */
    val items = listOf(
        BottomNavItem(
            route = Screen.Expenses.route,
            icon = R.drawable.ic_expenses,
            label = R.string.incomes,
            showFab = true
        ),
        BottomNavItem(
            route = Screen.Income.route,
            icon = R.drawable.ic_income,
            label = R.string.expenses,
            showFab = true
        ),
        BottomNavItem(
            route = Screen.Account.route,
            icon = R.drawable.ic_account,
            label = R.string.account,
        ),
        BottomNavItem(
            route = Screen.Category.route,
            icon = R.drawable.ic_article,
            label = R.string.categories
        ),
        BottomNavItem(
            route = Screen.Settings.route,
            icon = R.drawable.ic_settings,
            label = R.string.settings
        ),
    )
}