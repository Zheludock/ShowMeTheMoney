package com.example.showmethemoney.ui.screens.sections.subsections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.showmethemoney.data.AccountManager
import com.example.showmethemoney.data.FinanceRepository
import com.example.showmethemoney.data.dto.transaction.TransactionResponse
import com.example.showmethemoney.data.safecaller.ApiResult
import com.example.showmethemoney.data.safecaller.NetworkMonitor
import com.example.showmethemoney.data.safecaller.safeApiCall
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class TransactionViewModel @Inject constructor(
    private val repository: FinanceRepository,
    private val networkMonitor: NetworkMonitor,
    private val accountManager: AccountManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(TransactionUiState())
    val uiState: StateFlow<TransactionUiState> = _uiState.asStateFlow()

    private val _createTransactionResult = MutableStateFlow<ApiResult<TransactionResponse>?>(null)
    val createTransactionResult: StateFlow<ApiResult<TransactionResponse>?> = _createTransactionResult

    fun createTransaction(
        accountId: Int = accountManager.selectedAccountId,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String? = null
    ) {
        viewModelScope.launch {
            _createTransactionResult.value = safeApiCall(
                networkMonitor = networkMonitor,
                block = {
                    repository.createTransaction(
                        accountId = accountId,
                        categoryId = categoryId,
                        amount = amount,
                        transactionDate = transactionDate,
                        comment = comment
                    )
                }
            )
        }
    }

    fun updateAmount(amount: String) {
        _uiState.update { it.copy(amount = amount) }
    }

    fun updateTransactionDate(date: Date) {
        _uiState.update { it.copy(transactionDate = date) }
    }

    fun getFormattedDate(): String {
        return SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(_uiState.value.transactionDate)
    }

    fun getFormattedTime(): String {
        return SimpleDateFormat("HH:mm", Locale.getDefault()).format(_uiState.value.transactionDate)
    }

    fun updateComment(comment: String) {
        _uiState.update { it.copy(comment = comment) }
    }
}

data class TransactionUiState(
    val selectedAccountId: Int = 1,
    val accountName: String = "Сбербанк",
    val selectedCategoryId: Int = 1,
    val categoryName: String = "Ремонт",
    val amount: String = "",
    val transactionDate: Date = Date(),
    val comment: String = ""
) {
    fun getFormattedDate(): String {
        return SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(transactionDate)
    }

    fun getFormattedTime(): String {
        return SimpleDateFormat("HH:mm", Locale.getDefault()).format(transactionDate)
    }
}