package com.example.showmethemoney.ui.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.showmethemoney.R
import com.example.showmethemoney.navigation.Screen
import com.example.utils.TopBarState

/**
 * Кастомный TopAppBar с динамическими иконками действий и навигации.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    navController: NavController,
    topBarState: TopBarState
) {
    val currentRoute = navController.currentBackStackEntry?.destination?.route

    val actionIcon = when (currentRoute) {
        Screen.Expenses.route, Screen.Income.route -> R.drawable.ic_history

        Screen.Account.route -> R.drawable.ic_edit

        Screen.ExpenseHistory.route, Screen.IncomeHistory.route -> R.drawable.ic_history_calendar

        Screen.EditAccount.route, Screen.AddExpense.route, Screen.AddIncome.route,
            Screen.EditIncome.route, Screen.EditExpense.route -> R.drawable.ic_accept
        else -> null
    }

    val navigationIcon = when(currentRoute) {
        Screen.ExpenseHistory.route, Screen.IncomeHistory.route, Screen.ExpenseAnalysis.route,
        Screen.IncomeAnalysis.route, Screen.ColorSelection.route, Screen.About.route -> R.drawable.ic_back

        Screen.EditAccount.route, Screen.AddExpense.route, Screen.AddIncome.route,
            Screen.EditIncome.route, Screen.EditExpense.route -> R.drawable.ic_close

        else -> null
    }

    CenterAlignedTopAppBar(
        title = { Text(text = topBarState.title) },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        actions = {
            actionIcon?.let { iconRes ->
                IconButton(onClick = { topBarState.onActionClick?.invoke() }) {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = stringResource(R.string.right_topbar_icon)
                    )
                }
            }
        },
        navigationIcon = {
            navigationIcon?.let { iconRes ->
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = stringResource(R.string.navigation_icon)
                    )
                }
            }
        },
    )
}
