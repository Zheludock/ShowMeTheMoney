package com.example.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.runtime.Composable
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    showDialog: Boolean,
    onDismissRequest: () -> Unit,
    initialDate: Date,
    boundaryDate: Date? = null,
    isStartDatePicker: Boolean = true,
    onDateSelected: (Date) -> Unit,
    onClear: (() -> Unit)? = null
) {
    if (!showDialog) return

    CustomDatePickerDialog(
        initialDate = initialDate,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return if (boundaryDate == null) true
                else if (isStartDatePicker) {
                    utcTimeMillis <= boundaryDate.time
                } else {
                    utcTimeMillis >= boundaryDate.time
                }
            }
        },
        onClear = {
            onClear?.invoke()
            onDismissRequest()
        },
        onCancel = {
            onDismissRequest()
        },
        onConfirm = { newDate ->
            onDateSelected(newDate)
            onDismissRequest()
        }
    )
}