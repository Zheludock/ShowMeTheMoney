package com.example.transactions.transactionhistory

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.domain.response.ApiResult
import com.example.transactions.TransactionViewModel
import com.example.ui.DateUtils
import com.example.ui.ErrorView
import com.example.ui.LoadingIndicator
import com.example.ui.TopBarState
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
 * - [com.example.domain.response.ApiResult.Loading] - отображает индикатор загрузки
 * - [com.example.domain.response.ApiResult.Success] - показывает список транзакций или сообщение об отсутствии данных
 * - [com.example.domain.response.ApiResult.Error] - отображает ошибку с возможностью повтора
 *
 * ## Особенности
 * - Использует [ShowDatePickerDialog] для выбора дат
 * - Форматирует даты через [com.example.showmethemoney.ui.utils.DateUtils]
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
                navController = navController,
                isIncome = isIncome,
                DateUtils.formatDateForDisplay(startDateForUI),
                DateUtils.formatDateForDisplay(endDateForUI),
                onStartDateClick = { showStartDatePicker = true },
                onEndDateClick = { showEndDatePicker = true }
            )
        }

        is ApiResult.Error -> ErrorView(error = state.error, onRetry = onRetry)
    }
}
@Composable
fun IsIncomeFromNavigation(route: String?): Boolean {
    return when (route) {
        "income" -> true
        else -> false
    }
}