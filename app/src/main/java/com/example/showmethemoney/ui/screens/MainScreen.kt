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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.showmethemoney.R
import com.example.showmethemoney.navigation.AppNavHost
import com.example.showmethemoney.navigation.BottomNavItems
import com.example.showmethemoney.navigation.Screen
import com.example.showmethemoney.network.NetworkAwareViewModel
import com.example.showmethemoney.ui.components.AppBottomNavigation
import com.example.showmethemoney.ui.components.AppTopBar
import com.example.showmethemoney.ui.theme.IconsGreen
import com.example.showmethemoney.ui.theme.White
import kotlinx.coroutines.launch

@Composable
fun MainScreen(viewModelFactory: ViewModelProvider.Factory) {

    val viewModel: NetworkAwareViewModel = viewModel(factory = viewModelFactory)

    val isOnline by viewModel.isOnline.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Expenses.route

    val currentNavItem by remember(currentRoute) {
        derivedStateOf { BottomNavItems.items.firstOrNull { it.route == currentRoute } }
    }
    val showFab = currentNavItem?.showFab ?: false
    val currentTitle = Screen.fromRoute(currentRoute).title

    Scaffold(
        topBar = {
            AppTopBar(
                onActionIconClick = {
                    when (currentTitle) {
                        R.string.incomes_today, R.string.expenses_today ->
                            navController.navigate(Screen.History.route)
                        R.string.my_account -> { /* TODO */ }
                        else -> {}
                    }},
                navController = navController
                )
            },
        bottomBar = {
            AppBottomNavigation(
                navItems = BottomNavItems.items,
                currentRoute = currentRoute.toString(),
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
                    onClick = { when (currentRoute) { // Не жми, а то пойдешь в Runtime
                        Screen.Expenses.route -> navController
                            .navigate(Screen.AddExpense.route.toString())
                        Screen.Income.route -> navController
                            .navigate(Screen.AddExpense.route.toString())
                        else -> {}
                    } },
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
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            AppNavHost(navController = navController, viewModelFactory = viewModelFactory)
        }
        LaunchedEffect(isOnline) {
            if (!isOnline) {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Нет подключения к интернету",
                        duration = SnackbarDuration.Indefinite
                    )
                }
            } else {
                scope.launch {
                    snackbarHostState.currentSnackbarData?.dismiss()
                }
            }
        }
    }
}
