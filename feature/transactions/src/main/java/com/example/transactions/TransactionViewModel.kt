package com.example.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.TransactionDomain
import com.example.domain.response.ApiResult
import com.example.domain.usecase.transaction.GetTransactionsUseCase
import com.example.utils.AccountManager
import com.example.utils.DateUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel для работы с транзакциями.
 *
 * Отвечает за:
 * - Хранение и управление состоянием транзакций (загрузка, успех, ошибка)
 * - Фильтрацию транзакций по типу (доход/расход)
 * - Сортировку транзакций по дате (новые сначала)
 * - Управление датами периода для выборки транзакций
 * - Автоматическую перезагрузку транзакций при изменении параметров
 *
 * @property getTransactionsUseCase UseCase для получения транзакций с сервера
 */
class TransactionViewModel @Inject constructor(
    private val getTransactionsUseCase: GetTransactionsUseCase
) : ViewModel() {

    private val _startDateForUI = MutableStateFlow(DateUtils.formatCurrentDate())
    val startDateForUI: StateFlow<String> = _startDateForUI

    private val _endDateForUI = MutableStateFlow(DateUtils.formatCurrentDate())
    val endDateForUI: StateFlow<String> = _endDateForUI

    private val _transactions = MutableStateFlow<ApiResult<List<TransactionItem>>>(ApiResult.Loading)
    val transactions: StateFlow<ApiResult<List<TransactionItem>>> = _transactions

    private val _isIncome = MutableStateFlow(false)
    val isIncome: StateFlow<Boolean> = _isIncome

    fun updateIsIncome(isIncome: Boolean) {
        _isIncome.value = isIncome
    }

    fun updateStartDate(date: String) {
        _startDateForUI.value = date
    }

    fun updateEndDate(date: String) {
        _endDateForUI.value = date
    }

    fun loadTransactions(isIncome: Boolean,
                         startDate: String = startDateForUI.value,
                         endDate: String = endDateForUI.value) {
        viewModelScope.launch {
            _transactions.value = ApiResult.Loading
            val accountId = AccountManager.selectedAccountId
            val result = getTransactionsUseCase.execute(accountId, startDate, endDate)

            _transactions.value = when (result) {
                is ApiResult.Success -> {
                    val filtered = result.data
                        .map { it.toTransactionItem() }
                        .filter { it.isIncome == isIncome }
                        .sortedByDescending { it.createdAt }
                    ApiResult.Success(filtered)
                }
                is ApiResult.Error -> result
                ApiResult.Loading -> ApiResult.Loading
            }
        }
    }
}

fun TransactionDomain.toTransactionItem(): TransactionItem {
    return TransactionItem(
        id = id,
        categoryEmoji = emoji,
        categoryName = categoryName,
        categoryId = categoryId,
        comment = comment,
        amount = amount,
        accountCurrency = currency,
        createdAt = createdAt,
        isIncome = isIncome
    )
}