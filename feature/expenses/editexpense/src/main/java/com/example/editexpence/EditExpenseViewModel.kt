package com.example.editexpence

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.CategoryDomain
import com.example.domain.model.TransactionDomain
import com.example.domain.usecase.category.GetCategoriesByTypeUseCase
import com.example.domain.usecase.transaction.CreateTransactionUseCase
import com.example.domain.usecase.transaction.GetTransactionDetailsUseCase
import com.example.domain.usecase.transaction.UpdateTransactionUseCase
import com.example.ui.EditTransactionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

class EditExpenseViewModel @Inject constructor(
    private val updateTransactionUseCase: UpdateTransactionUseCase,
    private val getCategoriesByTypeUseCase: GetCategoriesByTypeUseCase,
    private val getTransactionDetailsUseCase: GetTransactionDetailsUseCase
) : ViewModel() {

    private val _expenseCategories = MutableStateFlow<List<CategoryDomain>>(emptyList())
    val expenseCategories: StateFlow<List<CategoryDomain>> = _expenseCategories.asStateFlow()

    private val _currentTransaction = MutableStateFlow<TransactionDomain?>(null)
    val currentTransaction: StateFlow<TransactionDomain?> = _currentTransaction

    var state by mutableStateOf(EditTransactionState())
        private set

    private val apiDateFormat = SimpleDateFormat(
        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
        Locale.getDefault()
    ).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    private val displayDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    private val displayTimeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    init {
        loadCategories()
    }

    fun loadTransactionById(id: Int) {
        viewModelScope.launch {
            _currentTransaction.value = getTransactionDetailsUseCase.execute(id)
        }
        if (currentTransaction.value != null) {
            state = state.copy(
                id = currentTransaction.value!!.id,
                categoryId = currentTransaction.value!!.categoryId,
                categoryName = currentTransaction.value!!.categoryName,
                amount = currentTransaction.value!!.amount,
                transactionDate = currentTransaction.value!!.transactionDate,
                comment = currentTransaction.value!!.comment
            )
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _expenseCategories.value = getCategoriesByTypeUseCase.execute(false)
        }
    }


    fun onCategorySelected(categoryId: Int, categoryName: String) {
        state = state.copy(categoryId = categoryId, categoryName = categoryName)
    }

    fun onAmountChange(amount: String) {
        state = state.copy(amount = amount)
    }

    fun updateDate(newDate: String) {
        val currentDateTime = parseApiDate(state.transactionDate)
        val newDateObj = apiDateFormat.parse(newDate) ?: Date()

        val calendar = Calendar.getInstance().apply {
            time = currentDateTime
            set(Calendar.YEAR, newDateObj.year + 1900)
            set(Calendar.MONTH, newDateObj.month)
            set(Calendar.DAY_OF_MONTH, newDateObj.date)
        }

        state = state.copy(
            transactionDate = apiDateFormat.format(calendar.time),
            displayDate = displayDateFormat.format(calendar.time)
        )
    }

    fun updateTime(newTime: String) {
        val currentDateTime = parseApiDate(state.transactionDate)
        val calendar = Calendar.getInstance().apply { time = currentDateTime }

        val (hours, minutes) = newTime.split(":").map { it.toInt() }
        calendar.set(Calendar.HOUR_OF_DAY, hours)
        calendar.set(Calendar.MINUTE, minutes)

        state = state.copy(
            transactionDate = apiDateFormat.format(calendar.time),
            displayTime = displayTimeFormat.format(calendar.time)
        )
    }

    private fun parseApiDate(dateString: String): Date {
        return apiDateFormat.parse(dateString) ?: Date()
    }

    fun onCommentChange(comment: String) {
        state = state.copy(comment = comment)
    }

    fun editTransaction() {
        val accountId = state.accountId
        val categoryId = state.categoryId
        val amount = state.amount
        val date = state.transactionDate

        if (amount.isBlank()) {
            return
        }
        val amountParsed = amount.replace(',', '.').toDoubleOrNull()
        if (amountParsed == null || amountParsed <= 0.0) {
            return
        }

        viewModelScope.launch {
            updateTransactionUseCase.execute(
                id = state.id,
                accountId = accountId,
                categoryId = categoryId,
                amount = amount,
                date = date,
                comment = state.comment
            )
        }
    }
}