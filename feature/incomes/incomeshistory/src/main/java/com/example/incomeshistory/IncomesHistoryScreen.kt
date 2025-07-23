package com.example.incomeshistory

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
import com.example.ui.TransactionTransfer
import com.example.utils.TopBarState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomesHistoryScreen(
    navController: NavController,
    viewModelFactory: ViewModelProvider.Factory,
    updateTopBar: (TopBarState) -> Unit
) {
    val viewModel: IncomesHistoryViewModel = viewModel(factory = viewModelFactory)

    LaunchedEffect(Unit) {
        updateTopBar(
            TopBarState(
                title = "Моя история",
                onActionClick = {
                    navController.navigate("income_analysis")
                }
            )
        )
    }

    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    val startDateForUI by viewModel.startDateForUI.collectAsState()
    val endDateForUI by viewModel.endDateForUI.collectAsState()

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

    val state = viewModel.transactions.collectAsState()

    TransactionHistoryList(
        transactions = state.value,
        startDate = startDateForUI,
        endDate = endDateForUI,
        onStartDateClick = { showStartDatePicker = true },
        onEndDateClick = { showEndDatePicker = true },
        onElementClick = { item ->
            TransactionTransfer.editedTransaction = item
            navController.navigate("edit_income")
        }
    )
}