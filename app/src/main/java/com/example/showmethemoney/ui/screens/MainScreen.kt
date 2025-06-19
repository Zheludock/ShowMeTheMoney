package com.example.showmethemoney.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.showmethemoney.navigation.AppNavHost
import com.example.showmethemoney.navigation.BottomNavItems
import com.example.showmethemoney.navigation.Screen
import com.example.showmethemoney.ui.components.AppBottomNavigation
import com.example.showmethemoney.ui.components.AppTopBar
import com.example.showmethemoney.ui.theme.IconsGreen
import com.example.showmethemoney.ui.theme.White

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Expenses.route

    val currentNavItem = BottomNavItems.items.firstOrNull { it.route == currentRoute }
    val showFab = currentNavItem?.showFab ?: false
    val currentTitle = Screen.fromRoute(currentRoute).title

    Scaffold(
        topBar = {
            AppTopBar(
                title = currentTitle,
                onActionIconClick = {
                    when (currentTitle) {
                        "Расходы сегодня", "Доходы сегодня" -> navController.navigate(Screen.History.route)
                        "Мой счет" -> { /* TODO */ }
                        else -> {}
                    }},
                navController = navController
                )
            },
        bottomBar = {
            AppBottomNavigation(
                navItems = BottomNavItems.items,
                currentRoute = currentRoute,
                onItemClick = { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            if (showFab) {
                FloatingActionButton(
                    onClick = { /* Действие при клике */ },
                    modifier = Modifier
                        .shadow(8.dp, CircleShape)
                        .size(56.dp),
                    shape = CircleShape,
                    containerColor = IconsGreen,
                    contentColor = White,
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 6.dp,
                        pressedElevation = 12.dp
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Добавить"
                    )
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            AppNavHost(navController = navController)
        }
    }
}
