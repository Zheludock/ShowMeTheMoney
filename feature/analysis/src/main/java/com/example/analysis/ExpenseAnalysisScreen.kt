package com.example.analysis

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.graphics.piechart.PieChart
import com.example.ui.DatePickerDialog
import com.example.ui.UniversalListItem
import com.example.utils.AccountManager
import com.example.utils.DateUtils
import com.example.utils.StringFormatter
import com.example.utils.TopBarState

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
    val stats by viewModel.stats.collectAsState()
    val totalSum by viewModel.totalSum.collectAsState()

    LaunchedEffect(isIncome) {
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
                trail = DateUtils.formatDateToString(startDateForUI) to null,
                onClick = { showStartDatePicker = true },
                modifier = Modifier.height(56.dp)
            )
        }
        item {
            UniversalListItem(
                content = "Период: конец" to null,
                trail = DateUtils.formatDateToString(endDateForUI) to null,
                onClick = { showEndDatePicker = true },
                modifier = Modifier.height(56.dp)
            )
        }
        item {
            UniversalListItem(
                content = "Сумма" to null,
                trail = StringFormatter.formatAmount(totalSum.toDouble(),
                    AccountManager.selectedAccountCurrency.value) to null,
                modifier = Modifier.height(56.dp)
            )
        }
        item {
            PieChart(
                accountId = AccountManager.selectedAccountId,
                startDate = startDateForUI,
                endDate = endDateForUI,
                isIncome = false,
                viewModelFactory = viewModelFactory
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.tertiary)
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
        initialDate = startDateForUI,
        boundaryDate = endDateForUI,
        isStartDatePicker = true,
        onDateSelected = { newDate ->
            viewModel.updateStartDate(newDate)
        },
        onClear = {
            viewModel.updateStartDate(endDateForUI)
        }
    )

    DatePickerDialog(
        showDialog = showEndDatePicker,
        onDismissRequest = { showEndDatePicker = false },
        initialDate = endDateForUI,
        boundaryDate = startDateForUI,
        isStartDatePicker = false,
        onDateSelected = { newDate ->
            viewModel.updateEndDate(newDate)
        },
        onClear = {
            viewModel.updateEndDate(startDateForUI)
        }
    )
}