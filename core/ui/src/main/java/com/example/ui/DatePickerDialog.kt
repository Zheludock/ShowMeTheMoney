package com.example.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    showDialog: Boolean,
    onDismissRequest: () -> Unit,
    dateFormat: SimpleDateFormat,
    initialDate: String,
    boundaryDate: String? = null,
    isStartDatePicker: Boolean = true,
    onDateSelected: (String) -> Unit,
    onClear: (() -> Unit)? = null
) {
    if (!showDialog) return

    val parsedInitialDate = remember(initialDate) {
        dateFormat.parse(initialDate) ?: Date()
    }

    val parsedBoundaryDate = remember(boundaryDate) {
        boundaryDate?.let { dateFormat.parse(it) } ?: Date()
    }

    CustomDatePickerDialog(
        initialDate = parsedInitialDate,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return if (boundaryDate == null) true
                else if (isStartDatePicker) {
                    utcTimeMillis <= parsedBoundaryDate.time
                } else {
                    utcTimeMillis >= parsedBoundaryDate.time
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
            val formattedDate = dateFormat.format(newDate)
            onDateSelected(formattedDate)
            onDismissRequest()
        }
    )
}