package com.example.showmethemoney.ui.utils

import androidx.compose.runtime.Composable
import com.example.showmethemoney.navigation.Screen
/**
 * Определяет, относится ли переданный маршрут навигации к экрану доходов.
 *
 * @param route Маршрут навигации для проверки. Если равен [Screen.Income.route],
 *              будет возвращено `true`, иначе - `false`.
 *
 * @return `true` если маршрут соответствует экрану доходов,
 *         `false` для всех остальных случаев, включая `null`.
 *
 * @sample Пример использования:
 * val isIncome = IsIncomeFromNavigation(navController.currentBackStackEntry?.destination?.route)
 */
