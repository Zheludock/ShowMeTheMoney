package com.example.expenseshistory

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.transaction.GetTransactionsUseCase
import com.example.ui.TransactionItem
import com.example.ui.toTransactionItem
import com.example.utils.AccountManager
import com.example.utils.DateUtils
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class ExpensesHistoryViewModel @Inject constructor(
    private val getTransactionsUseCase: GetTransactionsUseCase
) : ViewModel() {

    private val _startDateForUI = MutableStateFlow(DateUtils.getStartOfCurrentMonth())
    val startDateForUI: StateFlow<Date> = _startDateForUI

    private val _endDateForUI = MutableStateFlow(DateUtils.endOfDay(Date()))
    val endDateForUI: StateFlow<Date> = _endDateForUI

    private val _transactions = MutableStateFlow<List<TransactionItem>>(emptyList())
    val transactions: StateFlow<List<TransactionItem>> = _transactions

    fun updateStartDate(date: Date) {
        _startDateForUI.value = date
    }

    fun updateEndDate(date: Date) {
        _endDateForUI.value = date
    }

    init {
        viewModelScope.launch {
            combine(_startDateForUI, _endDateForUI) { startDate, endDate ->
                startDate to endDate
            }
                .flatMapLatest { (startDate, endDate) ->
                    getTransactionsUseCase.execute(AccountManager.selectedAccountId, startDate, endDate)
                        .catch { e ->
                            Log.e("TransactionsVM", "Error loading transactions", e)
                            emit(emptyList()) // в случае ошибки возвращаем пустой список
                        }
                }
                .collect { list ->
                    _transactions.value = list.map { it.toTransactionItem() }.filter { !it.isIncome }
                }
        }
    }
}

