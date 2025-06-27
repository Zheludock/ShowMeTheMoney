package com.example.showmethemoney.ui.screens.transactionhistory

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.showmethemoney.ui.components.CustomDatePickerDialog
import com.example.showmethemoney.ui.screens.transactions.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowDatePickerDialog(
    showDialog: Boolean,
    onDismissRequest: () -> Unit,
    dateFormat: SimpleDateFormat,
    isStartDatePicker: Boolean,
    viewModel: TransactionViewModel
) {
    if (!showDialog) return

    val initialDateForUI = if(isStartDatePicker) viewModel.startDateForUI
        else viewModel.endDateForUI
    val initialDate = remember(initialDateForUI) {
        dateFormat.parse(initialDateForUI.value) ?: Date()
    }
    val boundaryDateForUI = if(isStartDatePicker) viewModel.endDateForUI
        else viewModel.startDateForUI
    val boundaryDate = remember(boundaryDateForUI) {
        dateFormat.parse(boundaryDateForUI.value) ?: Date()
    }

    CustomDatePickerDialog(
        initialDate = initialDate,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return if (isStartDatePicker) {
                    utcTimeMillis <= boundaryDate.time
                } else {
                    utcTimeMillis >= boundaryDate.time
                }
            }
        },
        onClear = {
            if (isStartDatePicker) {
                viewModel.updateStartDate(viewModel.endDateForUI.value)
            } else {
                viewModel.updateEndDate(viewModel.endDateForUI.value)
            }
            viewModel.loadTransactions(
                isIncome = viewModel.isIncome.value,
                startDate = viewModel.startDateForUI.value,
                endDate = viewModel.endDateForUI.value
            )
            onDismissRequest()
        },
        onCancel = {
            onDismissRequest()
        },
        onConfirm = { newDate ->
            val formattedDate = dateFormat.format(newDate)
            if (isStartDatePicker) {
                viewModel.updateStartDate(formattedDate)
            } else {
                viewModel.updateEndDate(formattedDate)
            }
            viewModel.loadTransactions(
                isIncome = viewModel.isIncome.value,
                startDate = viewModel.startDateForUI.value,
                endDate = viewModel.endDateForUI.value
            )
            onDismissRequest()
        }
    )
}
