package com.example.showmethemoney.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.showmethemoney.ui.theme.IconsGreen
import com.example.showmethemoney.ui.theme.Indicator
import com.example.showmethemoney.ui.theme.SelectedTextUnderIcons
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
/**
 * Кастомный диалог выбора даты с кнопками "Clear", "Cancel" и "OK".
 *
 * @param initialDate Начальная выбранная дата.
 * @param selectableDates Ограничения на выбираемые даты (если null — все даты доступны).
 * @param onClear Колбэк при нажатии на "Clear" (сброс выбора).
 * @param onCancel Колбэк при нажатии на "Cancel" или закрытии диалога.
 * @param onConfirm Колбэк при нажатии на "OK" (передает выбранную дату).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePickerDialog(
    initialDate: Date,
    selectableDates: SelectableDates? = null,
    onClear: () -> Unit,
    onCancel: () -> Unit,
    onConfirm: (Date) -> Unit,
) {
    val dateFormat = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }

    val datePickerState = selectableDates?.let {
        rememberDatePickerState(
            initialSelectedDateMillis = initialDate.time,
            yearRange = 2024..2026,
            selectableDates = it
        )
    }

    DatePickerDialog(
        onDismissRequest = onCancel,
        confirmButton = { /* пусто, кнопка OK в dismissButton */ },
        dismissButton = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onClear,
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
                    onClick = onCancel,
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
                        datePickerState?.selectedDateMillis?.let {
                            onConfirm(Date(it))
                        }
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
        ),
        modifier = Modifier
    ) {
        datePickerState?.let {
            DatePicker(
                state = it,
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
}
