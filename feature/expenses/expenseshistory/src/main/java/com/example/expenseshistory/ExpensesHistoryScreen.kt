package com.example.expenseshistory

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
import com.example.ui.DatePickerDialog
import com.example.ui.TransactionHistoryList
import com.example.utils.TopBarState
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpensesHistoryScreen(
    navController: NavController,
    viewModelFactory: ViewModelProvider.Factory,
    updateTopBar: (TopBarState) -> Unit
) {
    val viewModel: ExpensesHistoryViewModel = viewModel(factory = viewModelFactory)

    LaunchedEffect(Unit) {
        updateTopBar(
            TopBarState(
                title = "Моя история"
            )
        )
    }

    LaunchedEffect(Unit) {
        viewModel.loadExpenses(
            viewModel.startDateForUI.value,
            viewModel.endDateForUI.value
        )
    }

    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    val dateFormat = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }

    val startDateForUI by viewModel.startDateForUI.collectAsState()
    val endDateForUI by viewModel.endDateForUI.collectAsState()

    DatePickerDialog(
        showDialog = showStartDatePicker,
        onDismissRequest = { showStartDatePicker = false },
        dateFormat = dateFormat,
        initialDate = startDateForUI,
        boundaryDate = endDateForUI,
        isStartDatePicker = true,
        onDateSelected = { newDate ->
            viewModel.updateStartDate(newDate)
            viewModel.loadExpenses(newDate, endDateForUI)
        },
        onClear = {
            viewModel.updateStartDate(endDateForUI)
            viewModel.loadExpenses(endDateForUI, endDateForUI)
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
            viewModel.loadExpenses(startDateForUI, newDate)
        },
        onClear = {
            viewModel.updateEndDate(startDateForUI)
            viewModel.loadExpenses(startDateForUI, startDateForUI)
        }
    )

    val state = viewModel.transactions.collectAsState()

    TransactionHistoryList(
        transactions = state.value,
        startDate = startDateForUI,
        endDate = endDateForUI,
        onStartDateClick = { showStartDatePicker = true },
        onEndDateClick = { showEndDatePicker = true },
        onElementClick = { item -> navController.navigate("edit_expense?transactionId=${item.id}") }
    )
}