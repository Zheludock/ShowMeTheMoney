package com.example.showmethemoney.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.example.showmethemoney.ui.components.AppBottomNavigation
import com.example.showmethemoney.ui.components.AppTopBar
import com.example.showmethemoney.ui.theme.IconsGreen
import com.example.showmethemoney.ui.theme.White
import com.example.ui.TopBarState
import kotlinx.coroutines.launch

/**
 * Этот экран содержит:
 * - Scaffold с TopBar, BottomNavigation и FloatingActionButton
 * - Навигацию между экранами через NavController
 * - Отслеживание состояния сети (онлайн/оффлайн)
 * - Отображение снекбара при отсутствии интернета
 *
 * @param viewModelFactory Фабрика для создания [NetworkAwareViewModel], которая должна содержать
 * необходимые зависимости для работы ViewModel (включая мониторинг сети)
 *
 * Структура экрана:
 * 1. [AppTopBar] - верхняя панель с заголовком и кнопками действий
 * 2. [AppBottomNavigation] - нижняя навигационная панель
 * 3. [FloatingActionButton] - кнопка добавления (отображается только на определенных экранах)
 * 4. [AppNavHost] - контейнер для навигации между экранами
 * 5. Snackbar - уведомление о статусе сети
 *
 * Особенности:
 * - Автоматически показывает/скрывает снекбар при изменении состояния сети
 * - FAB отображается только на экранах, где [BottomNavItem.showFab] = true
 * - Поддерживает "чистую" навигацию (singleTop, restoreState)
 * - Динамически изменяет заголовок и действия в TopBar в зависимости от текущего экрана
 *
 * Состояния:
 * - При отсутствии интернета показывает снекбар с предупреждением
 * - При восстановлении соединения автоматически скрывает снекбар
 */
@Composable
fun MainScreen(viewModelFactory: ViewModelProvider.Factory) {

    val viewModel: NetworkAwareViewModel = viewModel(factory = viewModelFactory)

    var topBarState by remember { mutableStateOf(TopBarState(title = "Расходы сегодня")) }

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

    Scaffold(
        topBar = { AppTopBar(navController, topBarState = topBarState) },
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
                    onClick = {
                        when (currentRoute) {
                            Screen.Expenses.route -> navController
                                .navigate(Screen.AddExpense.route)

                            Screen.Income.route -> navController
                                .navigate(Screen.AddIncome.route)

                            else -> {}
                        }
                    },
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
                        contentDescription = stringResource(R.string.add)
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            AppNavHost(updateTopBar = { newState ->
                topBarState = newState
            }, navController = navController, viewModelFactory = viewModelFactory)
        }
        val message = stringResource(R.string.no_internet_connection)
        LaunchedEffect(isOnline) {
            if (!isOnline) {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = message,
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