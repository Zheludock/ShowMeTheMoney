package com.example.showmethemoney.ui.screens.sections.subsections

import android.app.TimePickerDialog
import android.icu.util.Calendar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.showmethemoney.R
import com.example.showmethemoney.data.safecaller.ApiResult
import com.example.showmethemoney.navigation.Screen
import com.example.showmethemoney.ui.components.UniversalListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    navController: NavController,
    currentTransactionId: String? = null,
    viewModel: TransactionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isIncome = when (navController.previousBackStackEntry?.destination?.route) {
        Screen.Expenses.route -> false
        Screen.Income.route -> true
        else -> false
    }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.createTransactionResult.collect { result ->
            when (result) {
                is ApiResult.Success<*> -> navController.popBackStack()
                is ApiResult.Error -> {
                    println("Error: ${result.error}")
                }
                else -> {}
            }
        }
    }

    if (showDatePicker) {
        val calendar = Calendar.getInstance()
        calendar.time = uiState.transactionDate
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(
                state = rememberDatePickerState(
                    initialSelectedDateMillis = calendar.timeInMillis
                )
            )
        }
    }

    TransactionList(
        isIncome = isIncome,
        state = uiState,
        onAccountClick = { /* Открыть выбор счета */ },
        onCategoryClick = { /* Открыть выбор категории */ },
        onAmountChange = viewModel::updateAmount,
        onDateClick = { showDatePicker = true },
        onCommentChange = viewModel::updateComment,
        onCreateClick = {
            viewModel.createTransaction(
                categoryId = uiState.selectedCategoryId,
                amount = uiState.amount,
                transactionDate = viewModel.getFormattedDate(),
                comment = uiState.comment
            )
        }
    )
}

@Composable
fun TransactionList(
    isIncome: Boolean,
    state: TransactionUiState,
    onAccountClick: () -> Unit,
    onCategoryClick: () -> Unit,
    onAmountChange: (String) -> Unit,
    onDateClick: () -> Unit,
    onCommentChange: (String) -> Unit,
    onCreateClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 72.dp)
    ) {
        LazyColumn(Modifier.weight(1f)) {
            item {
                UniversalListItem(
                    content = "Счет" to null,
                    trail = state.accountName to {
                        Icon(
                            painter = painterResource(R.drawable.ic_more_vert),
                            contentDescription = "Выбрать счет",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable(onClick = onAccountClick)
                        )
                    }
                )
            }
            item {
                UniversalListItem(
                    content = "Статья" to null,
                    trail = state.categoryName to {
                        Icon(
                            painter = painterResource(R.drawable.ic_more_vert),
                            contentDescription = "Выбрать категорию",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable(onClick = onCategoryClick)
                        )
                    }
                )
            }
            item {
                UniversalListItem(
                    content = "Сумма" to null,
                    trail = state.amount to {
                        BasicTextField(
                            value = state.amount,
                            onValueChange = onAmountChange,
                            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }
                )
            }
            item {
                UniversalListItem(
                    content = "Дата" to null,
                    trail = state.getFormattedDate() to null
                )
            }
            item {
                UniversalListItem(
                    content = "Время" to null,
                    trail = state.getFormattedTime() to null
                )
            }
            item {
                UniversalListItem(
                    content = "Комментарий" to null,
                    trail = state.comment to {
                        BasicTextField(
                            value = state.comment,
                            onValueChange = onCommentChange,
                            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End)
                        )
                    }
                )
            }
        }

        Button(
            onClick = onCreateClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Создать транзакцию")
        }
    }
}
