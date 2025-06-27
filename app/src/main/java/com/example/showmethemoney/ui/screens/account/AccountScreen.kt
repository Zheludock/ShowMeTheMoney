package com.example.showmethemoney.ui.screens.account

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.domain.response.ApiResult
import com.example.showmethemoney.ui.components.ErrorView
import com.example.showmethemoney.ui.components.LoadingIndicator
/**
 * Экран отображения истории аккаунта.
 *
 * Этот экран загружает и отображает историю аккаунта, обрабатывая различные состояния загрузки.
 *
 * @param viewModelFactory Фабрика для создания [AccountViewModel], которая должна содержать
 * необходимые зависимости для работы ViewModel.
 *
 * Примечания:
 * - При первом вызове автоматически запускается загрузка истории через [AccountViewModel.loadAccountHistory]
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
fun AccountScreen(viewModelFactory: ViewModelProvider.Factory) {
    val viewModel: AccountViewModel = viewModel(factory = viewModelFactory)

    LaunchedEffect(Unit) {
        viewModel.loadAccountHistory()
    }
    val accountHistoryState by viewModel.accountHistory.collectAsState()

    when (val state = accountHistoryState) {
        is ApiResult.Loading -> LoadingIndicator()
        is ApiResult.Success -> AccountContent(state.data)
        is ApiResult.Error -> ErrorView(state.error) {
            viewModel.loadAccountHistory()
        }
    }
}
