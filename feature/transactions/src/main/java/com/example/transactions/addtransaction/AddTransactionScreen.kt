package com.example.transactions.addtransaction

import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.domain.model.CategoryDomain
import com.example.domain.model.TransactionDomain
import com.example.domain.response.ApiResult
import com.example.ui.CustomDatePickerDialog
import com.example.utils.DateUtils
import com.example.utils.TopBarState
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    viewModelFactory: ViewModelProvider.Factory,
    navController: NavController,
    updateTopBar: (TopBarState) -> Unit,
    currentTransactionId: Int? = null,
) {
    val viewModel: AddTransactionViewModel = viewModel(factory = viewModelFactory)

    val currentRoute = navController.currentBackStackEntry?.destination?.route?.substringBefore("?")
    val isIncome = currentRoute == "add_income"

    val uiState by viewModel.uiState.collectAsState()
    val incomeCategories by viewModel.incomeCategories.collectAsState()
    val expenseCategories by viewModel.expenseCategories.collectAsState()
    val showCategoryDialog by viewModel.showCategoryDialog.collectAsState()
    val currentTransaction by viewModel.currentTransaction.collectAsState()

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    LaunchedEffect(isIncome, currentTransactionId) {
        viewModel.updateIsIncome(isIncome)
        viewModel.loadCurrentTransaction(currentTransactionId)
    }

    val categoriesResult = if (isIncome) incomeCategories else expenseCategories
    val categories = (categoriesResult as? ApiResult.Success)?.data.orEmpty()

    LaunchedEffect(categories) {
        if (categories.isNotEmpty()) {
            viewModel.updateSelectedCategory(categories.first().categoryId, categories.first().categoryName)
        }
    }

    val transactionData = (currentTransaction as? ApiResult.Success)?.data

    LaunchedEffect(transactionData, categories) {
        if (transactionData != null) {
            viewModel.updateTransactionDate(transactionData.createdAt)
            viewModel.updateAmount(transactionData.amount)
            viewModel.updateComment(transactionData.comment ?: "")
            viewModel.updateSelectedCategory(transactionData.categoryId, transactionData.categoryName)
        } else {
            viewModel.updateAmount("")
            viewModel.updateComment("")
            viewModel.setCurrentDate()
            if (categories.isNotEmpty()) {
                viewModel.updateSelectedCategory(categories.first().categoryId, categories.first().categoryName)
            }
        }
    }

    LaunchedEffect(isIncome, uiState) {
        updateTopBar(
            TopBarState(
                title = if (isIncome) "Мои доходы" else "Мои расходы",
                onActionClick = {
                    if (currentTransactionId != null) {
                        viewModel.updateTransaction(
                            id = currentTransactionId,
                            categoryId = uiState.selectedCategoryId,
                            amount = uiState.amount,
                            date = uiState.transactionDate,
                            comment = uiState.comment
                        ) { navController.popBackStack() }
                    } else {
                        viewModel.createTransaction(
                            accountId = uiState.selectedAccountId,
                            categoryId = uiState.selectedCategoryId,
                            amount = uiState.amount,
                            transactionDate = uiState.transactionDate,
                            comment = uiState.comment
                        ) { navController.popBackStack() }
                    }
                }
            )
        )
    }

    val currentDate = remember(uiState.transactionDate) {
        DateUtils.parseDate(uiState.transactionDate) ?: Date()
    }

    if (showDatePicker) {
        CustomDatePickerDialog(
            initialDate = currentDate,
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean = utcTimeMillis <= currentDate.time
            },
            onClear = {
                viewModel.updateTransactionDate("")
                showDatePicker = false
            },
            onCancel = { showDatePicker = false },
            onConfirm = { date ->
                viewModel.updateTransactionDate(DateUtils.formatToUtc(date))
                showDatePicker = false
            }
        )
    }

    if (showTimePicker) {
        val calendar = Calendar.getInstance().apply { time = currentDate }
        TimePickerDialog(
            LocalContext.current,
            { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                viewModel.updateTransactionDate(DateUtils.formatToUtc(calendar.time))
                showTimePicker = false
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    if (showCategoryDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.showCategoryDialog(false) },
            title = { Text("Выберите категорию") },
            text = {
                LazyColumn {
                    items(categories) { category ->
                        ListItem(
                            headlineContent = { Text(category.categoryName) },
                            modifier = Modifier.clickable {
                                viewModel.updateSelectedCategory(category.categoryId, category.categoryName)
                            }
                        )
                        HorizontalDivider()
                    }
                }
            },
            confirmButton = {
                Button(onClick = { viewModel.showCategoryDialog(false) }) {
                    Text("Отмена")
                }
            }
        )
    }

    AddTransactionList(
        isIncome = isIncome,
        state = uiState,
        onCategoryClick = { viewModel.showCategoryDialog(true) },
        onAmountChange = viewModel::updateAmount,
        onDateClick = { showDatePicker = true },
        onTimeClick = { showTimePicker = true },
        onCommentChange = viewModel::updateComment,
        onDeleteClick = {
            currentTransactionId?.let {
                viewModel.deleteTransaction(it) { navController.popBackStack() }
            }
        },
        currentTransactionId = currentTransactionId
    )
}