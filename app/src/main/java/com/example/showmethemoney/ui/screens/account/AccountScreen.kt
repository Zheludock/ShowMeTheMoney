package com.example.showmethemoney.ui.screens.account

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.example.domain.response.ApiResult
import com.example.showmethemoney.navigation.Screen
import com.example.showmethemoney.ui.components.AppTopBar
import com.example.showmethemoney.ui.components.ErrorView
import com.example.showmethemoney.ui.components.LoadingIndicator
import com.example.showmethemoney.ui.utils.AccountManager

/**
 * Экран отображения истории аккаунта.
 *
 * Этот экран загружает и отображает историю аккаунта, обрабатывая различные состояния загрузки.
 *
 * @param viewModelFactory Фабрика для создания [AccountViewModel], которая должна содержать
 * необходимые зависимости для работы ViewModel.
 *
 * Примечания:
 * - При первом вызове автоматически запускается загрузка истории через [AccountViewModel.loadAccountDetails]
 * - Отображает индикатор загрузки во время запроса
 * - Показывает контент при успешной загрузке
 * - Отображает ошибку с возможностью повтора при неудачном запросе
 *
 * Состояния:
 * - [ApiResult.Loading] - Показывает [LoadingIndicator] во время загрузки данных
 * - [ApiResult.Success] - Отображает основной контент через [AccountContent] с полученными данными
 * - [ApiResult.Error] - Показывает [ErrorView] с сообщением об ошибке и кнопкой повтора
 */
@Composable
fun AccountScreen(
    viewModelFactory: ViewModelProvider.Factory,
    navController: NavController
) {
    val viewModel: AccountViewModel = viewModel(factory = viewModelFactory)

    LaunchedEffect(Unit) {
        viewModel.loadAccountDetails()
    }
    val accountDetailsState by viewModel.accountDetails.collectAsState()

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            AppTopBar(
                title = AccountManager.selectedAccountName.value,
                onActionIconClick = { navController.navigate(Screen.EditAccount.route) },
                navController = navController
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (val state = accountDetailsState) {
                is ApiResult.Loading -> LoadingIndicator()
                is ApiResult.Success -> AccountContent(state.data)
                is ApiResult.Error -> ErrorView(state.error) {
                    viewModel.loadAccountDetails()
                }
            }
        }
    }
}