package com.example.analysis

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.DatePickerDialog
import com.example.ui.UniversalListItem
import com.example.utils.AccountManager
import com.example.utils.DateUtils
import com.example.utils.StringFormatter
import com.example.utils.TopBarState
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ExpenseAnalysisScreen(
    viewModelFactory: ViewModelProvider.Factory,
    updateTopBar: (TopBarState) -> Unit
) {
    val viewModel: ExpenseAnalysisViewModel = viewModel(factory = viewModelFactory)

    val isIncome = false

    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }
    val startDateForUI by viewModel.startDateForUI.collectAsState()
    val endDateForUI by viewModel.endDateForUI.collectAsState()
    val dateFormat = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }
    val stats by viewModel.stats.collectAsState()
    val totalSum by viewModel.totalSum.collectAsState()

    LaunchedEffect(isIncome) {
        viewModel.loadTransactions(
            viewModel.startDateForUI.value,
            viewModel.endDateForUI.value,
            isIncome
        )
        updateTopBar(
            TopBarState(
                title = "Анализ"
            )
        )
    }

    LazyColumn {
        item {
            UniversalListItem(
                content = "Период: начало" to null,
                trail = DateUtils.formatDateForDisplay(startDateForUI) to null,
                onClick = { showStartDatePicker = true }
            )
        }
        item {
            UniversalListItem(
                content = "Период: конец" to null,
                trail = DateUtils.formatDateForDisplay(endDateForUI) to null,
                onClick = { showEndDatePicker = true }
            )
        }
        item {
            UniversalListItem(
                content = "Сумма" to null,
                trail = StringFormatter.formatAmount(totalSum.toDouble(),
                    AccountManager.selectedAccountCurrency.value) to null
            )
        }
        items(stats) { item ->
            val percent = ((item.amount.toDouble()/totalSum.toDouble()) * 100).toInt()
            UniversalListItem(
                lead = item.emoji,
                content = item.categoryName to null,
                trail = "$percent%\n${StringFormatter.formatAmount(item.amount.toDouble(),
                    AccountManager.selectedAccountCurrency.value)}" to null
            )
        }
    }

    DatePickerDialog(
        showDialog = showStartDatePicker,
        onDismissRequest = { showStartDatePicker = false },
        dateFormat = dateFormat,
        initialDate = startDateForUI,
        boundaryDate = endDateForUI,
        isStartDatePicker = true,
        onDateSelected = { newDate ->
            viewModel.updateStartDate(newDate)
            viewModel.loadTransactions(newDate, endDateForUI, isIncome)
        },
        onClear = {
            viewModel.updateStartDate(endDateForUI)
            viewModel.loadTransactions(endDateForUI, endDateForUI, isIncome)
        }
    )

    DatePickerDialog(
        showDialog = showEndDatePicker,
        onDismissRequest = { showEndDatePicker = false },
        dateFormat = dateFormat,
        initialDate = endDateForUI,
        boundaryDate = startDateForUI,
        isStartDatePicker = false,
        onDateSelected = { newDate ->
            viewModel.updateEndDate(newDate)
            viewModel.loadTransactions(startDateForUI, newDate, isIncome)
        },
        onClear = {
            viewModel.updateEndDate(startDateForUI)
            viewModel.loadTransactions(startDateForUI, startDateForUI, isIncome)
        }
    )
}