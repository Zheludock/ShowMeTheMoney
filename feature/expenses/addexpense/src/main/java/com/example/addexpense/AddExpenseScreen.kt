package com.example.addexpense

import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ui.DatePickerDialog
import com.example.ui.UniversalListItem
import com.example.utils.DateUtils
import com.example.utils.TopBarState
import java.util.Calendar
import java.util.Date

@Composable
fun AddExpenseScreen(
    viewModelFactory: ViewModelProvider.Factory,
    navController: NavController,
    updateTopBar: (TopBarState) -> Unit
) {
    val viewModel: AddExpenseViewModel = viewModel(factory = viewModelFactory)

    val categories = viewModel.expenseCategories
    var showCategoryDialog by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        updateTopBar(
            TopBarState(
                title = "Мои расходы",
                onActionClick = {
                    viewModel.createTransaction()
                }
            )
        )
    }

    LaunchedEffect(viewModel) {
        viewModel.transactionCreated.collect {
            navController.navigate("expenses") {
                popUpTo("add_expense")
            }
        }
    }

    val state = viewModel.state

    LazyColumn(Modifier.fillMaxSize()) {
        item {
            UniversalListItem(
                content = "Счет" to null,
                trail = state.accountName to null,
            )
        }
        item {
            UniversalListItem(
                content = "Статья" to null,
                trail = state.categoryName to {
                    Icon(
                        painter = painterResource(com.example.ui.R.drawable.ic_more_vert),
                        contentDescription = "Выбрать категорию",
                        modifier = Modifier.size(24.dp),
                    )
                },
                onClick = { showCategoryDialog = true }
            )
        }
        item {
            UniversalListItem(
                content = "Сумма" to null,
                trail = null to {
                    BasicTextField(
                        value = state.amount,
                        onValueChange = viewModel::onAmountChange,
                        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            )
        }
        item {
            UniversalListItem(
                content = "Дата" to null,
                trail = DateUtils.formatDateToString(state.transactionDate) to null,
                onClick = {
                    showDatePicker = true
                }
            )
        }
        item {
            UniversalListItem(
                content = "Время" to null,
                trail = DateUtils.formatTime(Date()) to null,
                onClick = {
                    showTimePicker = true
                }
            )
        }
        item {
            UniversalListItem(
                content = "Комментарий" to null,
                trail = null to {
                    BasicTextField(
                        value = state.comment ?: "",
                        onValueChange = viewModel::onCommentChange,
                        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End)
                    )
                }
            )
        }
    }

    DatePickerDialog(
        showDialog = showDatePicker,
        onDismissRequest = { showDatePicker = false },
        initialDate = state.transactionDate,
        onDateSelected = { newDate ->
            viewModel.updateDate(newDate)
        }
    )

    if (showTimePicker) {
        val calendar = Calendar.getInstance().apply { time = Date() }
        TimePickerDialog(
            LocalContext.current,
            { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                val updatedDate = calendar.time
                viewModel.updateDate(updatedDate)
                showTimePicker = false
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    if (showCategoryDialog) {
        AlertDialog(
            onDismissRequest = { showCategoryDialog = false },
            title = { Text("Выберите категорию") },
            text = {
                LazyColumn {
                    items(categories.value) { category ->
                        ListItem(
                            headlineContent = { Text(category.categoryName) },
                            modifier = Modifier.clickable {
                                viewModel.onCategorySelected(category.categoryId, category.categoryName)
                                showCategoryDialog =false
                            }
                        )
                        HorizontalDivider()
                    }
                }
            },
            confirmButton = {
                Button(onClick = { showCategoryDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }
}
