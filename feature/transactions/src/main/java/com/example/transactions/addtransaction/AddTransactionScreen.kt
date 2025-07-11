package com.example.transactions.addtransaction

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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.domain.model.CategoryDomain
import com.example.domain.model.TransactionDomain
import com.example.domain.response.ApiResult
import com.example.ui.CustomDatePickerDialog
import com.example.utils.TopBarState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

    LaunchedEffect(isIncome) {
        viewModel.updateIsIncome(isIncome)
        viewModel.loadCurrentTransaction(currentTransactionId)
    }

    val categories = if (isIncome) incomeCategories else expenseCategories

    val safeCategoryList: List<CategoryDomain> = when (categories) {
        is ApiResult.Success -> categories.data
        else -> emptyList()
    }

    LaunchedEffect(isIncome, safeCategoryList) {
        if (safeCategoryList.isNotEmpty()) {
            viewModel.updateSelectedCategory(
                safeCategoryList.first().categoryId,
                safeCategoryList.first().categoryName
            )
        }
    }

    val safeCurrentTransaction: TransactionDomain? = when (currentTransaction) {
        is ApiResult.Success -> (currentTransaction as ApiResult.Success<TransactionDomain>).data
        else -> null
    }


    LaunchedEffect(Unit) {
        updateTopBar(
            TopBarState(
                title = if (isIncome) "Мои доходы" else "Мои расходы",
                onActionClick = {
                    if (currentTransactionId != null) viewModel.updateTransaction(
                        id = currentTransactionId,
                        categoryId = uiState.selectedCategoryId,
                        amount = uiState.amount,
                        date = uiState.transactionDate,
                        comment = uiState.comment
                    ) {
                        navController.popBackStack()
                    }
                    else viewModel.createTransaction(
                        accountId = uiState.selectedAccountId,
                        categoryId = uiState.selectedCategoryId,
                        amount = uiState.amount,
                        transactionDate = uiState.transactionDate,
                        comment = uiState.comment
                    ) {
                        navController.popBackStack()
                    }
                }
            )
        )
    }

    LaunchedEffect(safeCurrentTransaction, safeCategoryList) {
        safeCurrentTransaction?.let {
            viewModel.updateTransactionDate(it.createdAt)
            viewModel.updateAmount(it.amount)
            viewModel.updateComment(it.comment ?: "")
            viewModel.updateSelectedCategory(
                categoryId = it.categoryId,
                categoryName = it.categoryName
            )
        } ?: run {
            viewModel.updateAmount("")
            viewModel.updateComment("")
            viewModel.setCurrentDate()
            if (safeCategoryList.isNotEmpty()) {
                viewModel.updateSelectedCategory(
                    safeCategoryList.first().categoryId,
                    safeCategoryList.first().categoryName
                )
            }
        }
    }

    val currentDate = remember {
        if (uiState.transactionDate.isNotEmpty()) {
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                .parse(uiState.transactionDate) ?: Date()
        } else {
            Date()
        }
    }

    if (showDatePicker) {
        CustomDatePickerDialog(
            initialDate = currentDate,
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return utcTimeMillis <= currentDate.time
                }
            },
            onClear = {
                viewModel.updateTransactionDate("")
                showDatePicker = false
            },
            onCancel = {
                showDatePicker = false
            },
            onConfirm = { date ->
                val dateFormat =
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                viewModel.updateTransactionDate(dateFormat.format(date))
                showDatePicker = false
            }
        )
    }

    if (showCategoryDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.showCategoryDialog(false) },
            title = { Text("Выберите категорию") },
            text = {
                LazyColumn {
                    items(safeCategoryList) { category ->
                        ListItem(
                            headlineContent = { Text(category.categoryName) },
                            modifier = Modifier.clickable {
                                viewModel.updateSelectedCategory(
                                    category.categoryId,
                                    categoryName = category.categoryName
                                )
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
        onCommentChange = viewModel::updateComment,
        onDeleteClick = {
            if (currentTransactionId != null)
                viewModel.deleteTransaction(currentTransactionId) {
                    navController.popBackStack()
                }
        },
        currentTransactionId = currentTransactionId
    )
}