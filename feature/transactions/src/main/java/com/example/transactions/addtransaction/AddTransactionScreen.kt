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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.domain.response.ApiResult
import com.example.ui.CustomDatePickerDialog
import com.example.utils.DateUtils
import com.example.utils.TopBarState
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    viewModelFactory: ViewModelProvider.Factory,
    navController: NavController,
    updateTopBar: (TopBarState) -> Unit,
    currentTransactionId: Int? = null,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

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

    // Обработчик жизненного цикла для отмены корутин при выходе с экрана
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                viewModel.resetPendingOperations()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Инициализация экрана
    LaunchedEffect(isIncome, currentTransactionId) {
        viewModel.updateIsIncome(isIncome)
        viewModel.loadCurrentTransaction(currentTransactionId)
    }

    val categoriesResult = if (isIncome) incomeCategories else expenseCategories
    val categories = (categoriesResult as? ApiResult.Success)?.data.orEmpty()

    // Инициализация категорий
    LaunchedEffect(categories) {
        if (categories.isNotEmpty() && uiState.selectedCategoryId == -1) {
            viewModel.updateSelectedCategory(categories.first().categoryId, categories.first().categoryName)
        }
    }

    val transactionData = (currentTransaction as? ApiResult.Success)?.data

    // Инициализация данных транзакции
    LaunchedEffect(transactionData, categories) {
        if (transactionData != null) {
            viewModel.updateFromTransaction(transactionData)
        } else {
            viewModel.updateAmount("")
            viewModel.updateComment("")
            viewModel.setCurrentDate()
            if (categories.isNotEmpty() && uiState.selectedCategoryId == -1) {
                viewModel.updateSelectedCategory(categories.first().categoryId, categories.first().categoryName)
            }
        }
    }

    // Настройка TopBar с фиксированным колбэком
    val saveAction = remember {
        {
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
    }

    LaunchedEffect(isIncome) {
        updateTopBar(
            TopBarState(
                title = if (isIncome) "Мои доходы" else "Мои расходы",
                onActionClick = saveAction
            )
        )
    }

    val currentDate = remember(uiState.transactionDate) {
        DateUtils.parseDate(uiState.transactionDate) ?: Date()
    }

    // Диалог выбора даты
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

    // Диалог выбора времени
    if (showTimePicker) {
        val calendar = Calendar.getInstance().apply { time = currentDate }
        TimePickerDialog(
            context,
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

    // Диалог выбора категории
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
                                viewModel.showCategoryDialog(false)
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

    // Основной контент
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