package com.example.showmethemoney.ui.screens.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.showmethemoney.R
import com.example.showmethemoney.navigation.Screen
import com.example.showmethemoney.ui.components.UniversalListItem
import com.example.showmethemoney.ui.theme.IconsGreen
import com.example.showmethemoney.ui.theme.Indicator
import com.example.showmethemoney.ui.theme.SelectedTextUnderIcons
import com.example.showmethemoney.ui.utils.TransactionItem
import com.example.showmethemoney.ui.utils.formatAmount
import com.example.showmethemoney.ui.utils.formatDate
import com.example.showmethemoney.ui.utils.formatDateForDisplay
import com.example.showmethemoney.ui.utils.formatTime
import com.example.showmethemoney.ui.utils.stringToDate
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    navController: NavController,
    viewModelFactory: ViewModelProvider.Factory) {
    val viewModel: ExpensesViewModel = viewModel(factory = viewModelFactory)

    val isIncome = when (navController.previousBackStackEntry?.destination?.route) {
        Screen.Expenses.route -> false
        Screen.Income.route -> true
        else -> false
    }

    LaunchedEffect(isIncome) {
        viewModel.loadTransactions(isIncome = isIncome)
    }

    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    val dateFormat = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }

    val startDateForUI by viewModel.startDateForUI.collectAsState()
    val endDateForUI by viewModel.endDateForUI.collectAsState()

    if (showStartDatePicker) {
        val currentEndDate = remember(viewModel.endDateForUI) {
            dateFormat.parse(viewModel.endDateForUI.value) ?: Date()
        }
        val initialStartDate = remember(viewModel.startDateForUI) {
            dateFormat.parse(viewModel.startDateForUI.value) ?: Date()
        }

        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = initialStartDate.time,
            yearRange = IntRange(2024, 2026),
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return utcTimeMillis <= currentEndDate.time
                }
            }
        )

        DatePickerDialog(
            onDismissRequest = { showStartDatePicker = false },
            confirmButton = {  },
            dismissButton = {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            viewModel.updateStartDate(endDateForUI)
                            viewModel.loadTransactions(isIncome = isIncome)
                            showStartDatePicker = false
                        },
                        modifier = Modifier.background(Indicator),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Indicator,
                            contentColor = SelectedTextUnderIcons
                        )
                    ) {
                        Text("Clear")
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = { showStartDatePicker = false },
                        modifier = Modifier.background(Indicator),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Indicator,
                            contentColor = SelectedTextUnderIcons
                        )
                    ) {
                        Text("Cancel")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                val newDate = Date(millis)
                                viewModel.updateStartDate(dateFormat.format(newDate))
                                viewModel.loadTransactions(isIncome = isIncome)
                            }
                            showStartDatePicker = false
                        },
                        modifier = Modifier.background(Indicator),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Indicator,
                            contentColor = SelectedTextUnderIcons
                        )
                    ) {
                        Text("OK")
                    }
                }
            },
            colors = DatePickerDefaults.colors(
                containerColor = Indicator,
                selectedDayContainerColor = IconsGreen
            )
        ) {
            DatePicker(
                state = datePickerState,
                title = null,
                headline = null,
                showModeToggle = false,
                colors = DatePickerDefaults.colors(
                    containerColor = Indicator,
                    selectedDayContainerColor = IconsGreen,
                    selectedYearContainerColor = IconsGreen,
                    selectedYearContentColor = SelectedTextUnderIcons,
                    todayContentColor = SelectedTextUnderIcons,
                    todayDateBorderColor = IconsGreen,
                    selectedDayContentColor = SelectedTextUnderIcons
                )
            )
        }
    }

    if (showEndDatePicker) {
        val currentStartDate = remember(viewModel.startDateForUI) {
            dateFormat.parse(viewModel.startDateForUI.value) ?: Date()
        }
        val initialEndDate = remember(viewModel.endDateForUI) {
            dateFormat.parse(viewModel.endDateForUI.value) ?: Date()
        }

        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = initialEndDate.time,
            yearRange = IntRange(2000, 2100),
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return utcTimeMillis >= currentStartDate.time
                }
            }
        )

        DatePickerDialog(
            onDismissRequest = { showEndDatePicker = false },
            confirmButton = {  },
            dismissButton = {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            viewModel.updateEndDate(endDateForUI)
                            viewModel.loadTransactions(isIncome = isIncome)
                            showEndDatePicker = false
                        },
                        modifier = Modifier.background(Indicator),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Indicator,
                            contentColor = SelectedTextUnderIcons
                        )
                    ) {
                        Text("Clear")
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = { showEndDatePicker = false },
                        modifier = Modifier.background(Indicator),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Indicator,
                            contentColor = SelectedTextUnderIcons
                        )
                    ) {
                        Text("Cancel")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                val newDate = Date(millis)
                                viewModel.updateEndDate(dateFormat.format(newDate))
                                viewModel.loadTransactions(isIncome = isIncome)
                            }
                            showEndDatePicker = false
                        },
                        modifier = Modifier.background(Indicator),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Indicator,
                            contentColor = SelectedTextUnderIcons
                        )
                    ) {
                        Text("OK")
                    }
                }
            },
            colors = DatePickerDefaults.colors(
                containerColor = Indicator,
                selectedDayContainerColor = IconsGreen
            )
        ) {
            DatePicker(
                state = datePickerState,
                title = null,
                headline = null,
                showModeToggle = false,
                colors = DatePickerDefaults.colors(
                    containerColor = Indicator,
                    selectedDayContainerColor = IconsGreen,
                    selectedYearContainerColor = IconsGreen,
                    selectedYearContentColor = SelectedTextUnderIcons,
                    todayContentColor = SelectedTextUnderIcons,
                    todayDateBorderColor = IconsGreen,
                    selectedDayContentColor = SelectedTextUnderIcons
                )
            )
        }
    }

    /*if (isIncome) {
        val incomesState by viewModel.incomes.collectAsState()
        when (val state = incomesState) {
            ApiResult.Loading -> LoadingIndicator()
            ApiResult.Success(state) -> {
                if (state.data.isNotEmpty()) {
                    IncomeHistoryList(
                        incomes = state.data,
                        formatDateForDisplay(startDateForUI),
                        formatDateForDisplay(endDateForUI),
                        onStartDateClick = { showStartDatePicker = true },
                        onEndDateClick = { showEndDatePicker = true }
                    )
                } else {
                    Text("Нет данных о доходах")
                }
            }
            is ApiResult.Error -> ErrorView(
                error = state.error,
                onRetry = { viewModel.loadTransactions(isIncome = true) }
            )
        }
    } else {
        val expensesState by viewModel.expenses.collectAsState()
        when (val state = expensesState) {
            is ApiResult.Loading -> LoadingIndicator()
            is ApiResult.Success -> {
                if (state.data.isNotEmpty()) {
                    ExpenseHistoryList(
                        expenses = state.data,
                        formatDateForDisplay(startDateForUI),
                        formatDateForDisplay(endDateForUI),
                        onStartDateClick = { showStartDatePicker = true },
                        onEndDateClick = { showEndDatePicker = true }
                    )
                } else {
                    Text("Нет данных о расходах")
                }
            }
            is ApiResult.Error -> ErrorView(
                error = state.error,
                onRetry = { viewModel.loadTransactions(isIncome = false) }
            )
        }
    }*/
}

@Composable
fun ExpenseHistoryList(
    expenses: List<TransactionItem>,
    startDate: String,
    endDate: String,
    onStartDateClick: () -> Unit,
    onEndDateClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        item {
            UniversalListItem(
                content = "Начало" to null,
                trail = startDate to null,
                modifier = Modifier
                    .background(Indicator)
                    .height(56.dp)
                    .padding(bottom = 1.dp),
                onClick = { onStartDateClick() }
            )
        }
        item {
            UniversalListItem(
                content = "Конец" to null,
                trail = endDate to null,
                modifier = Modifier
                    .background(Indicator)
                    .height(56.dp)
                    .padding(bottom = 1.dp),
                onClick = { onEndDateClick() }
            )
        }
        item {
            UniversalListItem(
                content = "Сумма" to null,
                trail = formatAmount(expenses.sumOf { it.amount.toDoubleOrNull() ?: 0.0 },
                    expenses.firstOrNull()?.accountCurrency ?: "RUB") to null,
                modifier = Modifier
                    .background(Indicator)
                    .height(56.dp)
                    .padding(bottom = 1.dp)
            )
        }
        items(expenses) { item ->
            UniversalListItem(
                lead = item.categoryEmoji,
                content = item.categoryName to item.comment,
                trail = buildString {
                    append(formatAmount(item.amount.toDoubleOrNull() ?: 0.0, item.accountCurrency))
                    append("\n")
                    item.createdAt?.let { createdAt ->
                        stringToDate(createdAt)?.let { date ->
                            append(formatDateForDisplay(formatDate(date)))
                            append(" ${formatTime(date)}")
                        }
                    }
                } to {
                    Icon(
                        painter = painterResource(R.drawable.ic_more_vert),
                        contentDescription = "Подробнее",
                        modifier = Modifier.size(24.dp)
                    )
                }
            )
        }
    }
}

@Composable
fun IncomeHistoryList(
    incomes: List<TransactionItem>,
    startDate: String,
    endDate: String,
    onStartDateClick: () -> Unit,
    onEndDateClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        item {
            UniversalListItem(
                content = "Начало" to null,
                trail = startDate to null,
                modifier = Modifier
                    .background(Indicator)
                    .height(56.dp)
                    .padding(bottom = 1.dp),
                onClick = { onStartDateClick() }
            )
        }
        item {
            UniversalListItem(
                content = "Конец" to null,
                trail = endDate to null,
                modifier = Modifier
                    .background(Indicator)
                    .height(56.dp)
                    .padding(bottom = 1.dp),
                onClick = { onEndDateClick() }
            )
        }
        item {
            UniversalListItem(
                content = "Сумма" to null,
                trail = formatAmount(incomes.sumOf { it.amount.toDoubleOrNull() ?: 0.0 },
                    incomes.firstOrNull()?.accountCurrency ?: "RUB") to null,
                modifier = Modifier
                    .background(Indicator)
                    .height(56.dp)
                    .padding(bottom = 1.dp)
            )
        }
        items(incomes) { item ->
            UniversalListItem(
                content = item.categoryName to item.comment,
                trail = buildString {
                    append(formatAmount(item.amount.toDoubleOrNull() ?: 0.0, item.accountCurrency))
                    append("\n")
                    item.createdAt?.let { createdAt ->
                        stringToDate(createdAt)?.let { date ->
                            append(formatDateForDisplay(formatDate(date)))
                            append(" ${formatTime(date)}")
                        }
                    }
                } to {
                    Icon(
                        painter = painterResource(R.drawable.ic_more_vert),
                        contentDescription = "Подробнее",
                        modifier = Modifier.size(24.dp)
                    )
                }
            )
        }
    }
}