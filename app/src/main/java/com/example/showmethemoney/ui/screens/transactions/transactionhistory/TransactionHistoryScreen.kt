package com.example.showmethemoney.ui.screens.transactions.transactionhistory

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.showmethemoney.ui.screens.TopBarState
import com.example.showmethemoney.ui.screens.transactions.TransactionViewModel
import com.example.showmethemoney.ui.utils.DateUtils
import com.example.showmethemoney.ui.utils.IsIncomeFromNavigation
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Экран истории транзакций (доходов/расходов) с возможностью выбора периода.
 *
 * Этот экран:
 * 1. Определяет тип транзакций (доходы/расходы) на основе предыдущего маршрута навигации
 * 2. Загружает и отображает список транзакций за выбранный период
 * 3. Позволяет выбирать даты начала и конца периода через диалоговые окна
 * 4. Обрабатывает различные состояния загрузки данных
 *
 * @param navController Контроллер навигации для определения типа транзакций
 * @param viewModelFactory Фабрика для создания [TransactionViewModel]
 *
 * ## Логика работы
 * - При запуске автоматически загружает транзакции:
 *   - С датой начала = первый день текущего месяца
 *   - С датой конца = текущая дата
 * - Обновляет данные при изменении типа транзакций или периода
 * - Показывает индикатор загрузки во время запроса данных
 *
 * ## Состояния экрана
 * - [ApiResult.Loading] - отображает индикатор загрузки
 * - [ApiResult.Success] - показывает список транзакций или сообщение об отсутствии данных
 * - [ApiResult.Error] - отображает ошибку с возможностью повтора
 *
 * ## Особенности
 * - Использует [ShowDatePickerDialog] для выбора дат
 * - Форматирует даты через [DateUtils]
 * - Динамически меняет текст при отсутствии данных в зависимости от типа транзакций
 * - Поддерживает повторную загрузку при ошибках
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionHistoryScreen(
    navController: NavController,
    viewModelFactory: ViewModelProvider.Factory,
    updateTopBar: (TopBarState) -> Unit
) {
    val viewModel: TransactionViewModel = viewModel(factory = viewModelFactory)

    val isIncome = IsIncomeFromNavigation(
        navController
            .previousBackStackEntry
            ?.destination
            ?.route
    )

    LaunchedEffect(Unit) {
        updateTopBar(
            TopBarState(
                title = "Моя история"
            )
        )
    }

    LaunchedEffect(isIncome) {
        viewModel.updateIsIncome(isIncome)
        viewModel.updateStartDate(DateUtils.getFirstDayOfCurrentMonth())
        viewModel.loadTransactions(
            isIncome, viewModel.startDateForUI.value,
            viewModel.endDateForUI.value
        )
    }

    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    val dateFormat = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }

    val startDateForUI by viewModel.startDateForUI.collectAsState()
    val endDateForUI by viewModel.endDateForUI.collectAsState()

    ShowDatePickerDialog(
        showDialog = showStartDatePicker,
        onDismissRequest = { showStartDatePicker = false },
        dateFormat = dateFormat,
        isStartDatePicker = true,
        viewModel = viewModel,
    )

    ShowDatePickerDialog(
        showDialog = showEndDatePicker,
        onDismissRequest = { showEndDatePicker = false },
        dateFormat = dateFormat,
        isStartDatePicker = false,
        viewModel = viewModel,
    )

    val state = viewModel.transactions.collectAsState().value
    val onRetry = {
        viewModel.loadTransactions(
            isIncome = isIncome,
            startDate = startDateForUI,
            endDate = endDateForUI
        )
    }

    when (state) {
        ApiResult.Loading -> LoadingIndicator()
        is ApiResult.Success -> {
            TransactionHistoryList(
                transactions = state.data,
                DateUtils.formatDateForDisplay(startDateForUI),
                DateUtils.formatDateForDisplay(endDateForUI),
                onStartDateClick = { showStartDatePicker = true },
                onEndDateClick = { showEndDatePicker = true }
            )
        }

        is ApiResult.Error -> ErrorView(error = state.error, onRetry = onRetry)
    }
}
