package com.example.showmethemoney.ui.screens.sections

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
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
import com.example.domain.ApiResult
import com.example.showmethemoney.ui.components.CustomDatePickerDialog
import com.example.showmethemoney.ui.components.ErrorView
import com.example.showmethemoney.ui.components.IsIncomeFromNavigation
import com.example.showmethemoney.ui.components.LoadingIndicator
import com.example.showmethemoney.ui.components.TransactionHistoryList
import com.example.showmethemoney.ui.utils.formatDateForDisplay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//Подумать о доработке
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    navController: NavController,
    viewModelFactory: ViewModelProvider.Factory
) {
    val viewModel: ExpensesViewModel = viewModel(factory = viewModelFactory)

    val isIncome = IsIncomeFromNavigation(navController.previousBackStackEntry?.destination?.route)

    LaunchedEffect(isIncome) {
        viewModel.loadTransactions(isIncome = isIncome)
    }

    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    val dateFormat = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }

    val startDateForUI by viewModel.startDateForUI.collectAsState()
    val endDateForUI by viewModel.endDateForUI.collectAsState()

    if (showStartDatePicker) {
        val currentEndDate = remember(endDateForUI) {
            dateFormat.parse(endDateForUI) ?: Date()
        }
        val initialStartDate = remember(startDateForUI) {
            dateFormat.parse(startDateForUI) ?: Date()
        }

        CustomDatePickerDialog(
            initialDate = initialStartDate,
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return utcTimeMillis <= currentEndDate.time
                }
            },
            onClear = {
                viewModel.updateStartDate(endDateForUI)
                viewModel.loadTransactions(isIncome = isIncome)
                showStartDatePicker = false
            },
            onCancel = {
                showStartDatePicker = false
            },
            onConfirm = { newDate ->
                viewModel.updateStartDate(dateFormat.format(newDate))
                viewModel.loadTransactions(isIncome = isIncome)
                showStartDatePicker = false
            }
        )
    }

    if (showEndDatePicker) {
        val currentStartDate = remember(startDateForUI) {
            dateFormat.parse(startDateForUI) ?: Date()
        }
        val initialEndDate = remember(endDateForUI) {
            dateFormat.parse(endDateForUI) ?: Date()
        }

        CustomDatePickerDialog(
            initialDate = initialEndDate,
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return utcTimeMillis >= currentStartDate.time
                }
            },
            onClear = {
                viewModel.updateEndDate(endDateForUI)
                viewModel.loadTransactions(isIncome = isIncome)
                showEndDatePicker = false
            },
            onCancel = {
                showEndDatePicker = false
            },
            onConfirm = { newDate ->
                viewModel.updateEndDate(dateFormat.format(newDate))
                viewModel.loadTransactions(isIncome = isIncome)
                showEndDatePicker = false
            }
        )
    }

    val state = if (isIncome) viewModel.incomes.collectAsState().value
    else viewModel.expenses.collectAsState().value
    val noDataText = if (isIncome) "Нет данных о доходах" else "Нет данных о расходах"
    val onRetry = { viewModel.loadTransactions(isIncome = isIncome) }

    when (state) {
        ApiResult.Loading -> LoadingIndicator()
        is ApiResult.Success -> {
            if (state.data.isNotEmpty()) {
                TransactionHistoryList(
                    transactions = state.data,
                    formatDateForDisplay(startDateForUI),
                    formatDateForDisplay(endDateForUI),
                    onStartDateClick = { showStartDatePicker = true },
                    onEndDateClick = { showEndDatePicker = true }
                )
            } else {
                Text(noDataText)
            }
        }
        is ApiResult.Error -> ErrorView(error = state.error, onRetry = onRetry)
    }
}
