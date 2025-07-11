package com.example.account

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.domain.response.ApiResult
import com.example.ui.ErrorView
import com.example.ui.LoadingIndicator
import com.example.utils.AccountManager
import com.example.utils.TopBarState

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
 * - [ApiResult.Loading] - Показывает [com.example.showmethemoney.ui.components.LoadingIndicator] во время загрузки данных
 * - [ApiResult.Success] - Отображает основной контент через [AccountContent] с полученными данными
 * - [ApiResult.Error] - Показывает [com.example.showmethemoney.ui.components.ErrorView] с сообщением об ошибке и кнопкой повтора
 */
@Composable
fun AccountScreen(
    viewModelFactory: ViewModelProvider.Factory,
    navController: NavController,
    updateTopBar: (TopBarState) -> Unit
) {
    val viewModel: AccountViewModel = viewModel(factory = viewModelFactory)

    LaunchedEffect(Unit) {
        viewModel.loadAccountDetails()
    }
    val accountDetailsState by viewModel.accountDetails.collectAsState()

    LaunchedEffect(Unit) {
        updateTopBar(
            TopBarState(
                title = AccountManager.selectedAccountName.value,
                onActionClick = { navController.navigate("edit_account") }
            )
        )
    }

    when (val state = accountDetailsState) {
        is ApiResult.Loading -> LoadingIndicator()
        is ApiResult.Success -> AccountContent(state.data)
        is ApiResult.Error -> ErrorView(state.error) {
            viewModel.loadAccountDetails()
        }
    }
}