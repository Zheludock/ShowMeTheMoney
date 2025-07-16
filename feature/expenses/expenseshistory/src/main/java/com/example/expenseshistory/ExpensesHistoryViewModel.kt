package com.example.expenseshistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.transaction.GetTransactionsUseCase
import com.example.ui.TransactionItem
import com.example.ui.toTransactionItem
import com.example.utils.AccountManager
import com.example.utils.DateUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ExpensesHistoryViewModel@Inject constructor(
    private val getTransactionsUseCase: GetTransactionsUseCase
) : ViewModel() {

    private val _startDateForUI = MutableStateFlow(DateUtils.getFirstDayOfCurrentMonth())
    val startDateForUI: StateFlow<String> = _startDateForUI

    private val _endDateForUI = MutableStateFlow(DateUtils.formatCurrentDate())
    val endDateForUI: StateFlow<String> = _endDateForUI

    private val _transactions = MutableStateFlow<List<TransactionItem>>(emptyList())
    val transactions: StateFlow<List<TransactionItem>> = _transactions

    fun updateStartDate(date: String) {
        _startDateForUI.value = date
    }

    fun updateEndDate(date: String) {
        _endDateForUI.value = date
    }

    fun loadExpenses(startDate: String = startDateForUI.value,
                         endDate: String = endDateForUI.value) {
        viewModelScope.launch {
            val accountId = AccountManager.selectedAccountId
            val result = getTransactionsUseCase.execute(accountId, startDate, endDate)
                .filter { !it.isIncome }
                .map { it.toTransactionItem() }
                .sortedByDescending { it.createdAt }
            _transactions.value = result
        }
    }
}
