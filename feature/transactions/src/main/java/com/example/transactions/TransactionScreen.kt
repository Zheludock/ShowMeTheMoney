package com.example.transactions

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
import com.example.utils.TopBarState

/**
 * Composable-функция, представляющая экран списка транзакций (доходов/расходов).
 *
 * Основные функции:
 * - Отображает состояние загрузки данных (лоадер)
 * - Показывает список транзакций при успешной загрузке
 * - Отображает ошибку при неудачном запросе с возможностью повтора
 * - Автоматически обновляет список при изменении типа транзакций (доход/расход)
 *
 * @param viewModelFactory Фабрика для создания ViewModel с зависимостями
 * @param isIncome Флаг типа транзакций:
 *   - true: отображаются доходы
 *   - false: отображаются расходы
 *
 * Состояния экрана:
 * - LoadingIndicator: отображается во время загрузки данных
 * - TransactionList: основной список транзакций (при успешной загрузке)
 * - ErrorView: сообщение об ошибке с кнопкой повтора (при ошибке загрузки)
 *
 * Логика работы:
 * 1. При инициализации получает ViewModel через фабрику
 * 2. При изменении isIncome обновляет фильтрацию в ViewModel
 * 3. Запускает загрузку транзакций во ViewModel
 * 4. Подписывается на состояние транзакций из ViewModel
 * 5. Рендерит соответствующее UI в зависимости от состояния
 */
@Composable
fun TransactionScreen(
    viewModelFactory: ViewModelProvider.Factory,
    isIncome: Boolean,
    navController: NavController,
    updateTopBar: (TopBarState) -> Unit
) {
    val viewModel: TransactionViewModel = viewModel(factory = viewModelFactory)

    LaunchedEffect(isIncome) {
        viewModel.updateIsIncome(isIncome)
        viewModel.loadTransactions(isIncome)
    }

    LaunchedEffect(Unit) {
        updateTopBar(
            TopBarState(
                title = if (isIncome) "Доходы сегодня" else "Расходы сегодня",
                onActionClick = { navController.navigate("history") }
            )
        )
    }

    val transactionState by viewModel.transactions.collectAsState()

    when (val state = transactionState) {
        is ApiResult.Loading -> LoadingIndicator()
        is ApiResult.Success -> TransactionList(state.data, navController,isIncome)
        is ApiResult.Error -> ErrorView(state.error) {
            viewModel.loadTransactions(
                viewModel.isIncome.value,
                viewModel.startDateForUI.value,
                viewModel.endDateForUI.value
            )
        }
    }
}




