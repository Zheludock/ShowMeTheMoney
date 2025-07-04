package com.example.showmethemoney.ui.screens.transactions

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.domain.response.ApiResult
import com.example.showmethemoney.R
import com.example.showmethemoney.navigation.Screen
import com.example.showmethemoney.ui.components.AppTopBar
import com.example.showmethemoney.ui.components.ErrorView
import com.example.showmethemoney.ui.components.LoadingIndicator
import com.example.showmethemoney.ui.screens.account.AccountContent

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
    navController: NavController
) {
    val viewModel: TransactionViewModel = viewModel(factory = viewModelFactory)

    LaunchedEffect(isIncome) {
        viewModel.updateIsIncome(isIncome)
        viewModel.loadTransactions(isIncome)
    }

    val transactionState by viewModel.transactions.collectAsState()

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            AppTopBar(
                title = if(isIncome) stringResource(R.string.incomes_today)
                        else stringResource(R.string.expenses_today),
                onActionIconClick = { navController.navigate(Screen.History.route) },
                navController = navController
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (val state = transactionState) {
                is ApiResult.Loading -> LoadingIndicator()
                is ApiResult.Success -> TransactionList(state.data)
                is ApiResult.Error -> ErrorView(state.error) {
                    viewModel.loadTransactions(
                        viewModel.isIncome.value,
                        viewModel.startDateForUI.value,
                        viewModel.endDateForUI.value
                    )
                }
            }
        }
    }
}



