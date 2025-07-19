package com.example.expenses

import android.util.Log
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

class ExpensesViewModel @Inject constructor(
    private val getTransactionsUseCase: GetTransactionsUseCase
): ViewModel() {
    val startDate = DateUtils.formatCurrentDate()
    val endDate = DateUtils.formatCurrentDate()

    private val _expenses = MutableStateFlow<List<TransactionItem>>(emptyList())
    val expenses: StateFlow<List<TransactionItem>> = _expenses

    fun loadExpenses(){
        viewModelScope.launch {
            val accountId = AccountManager.selectedAccountId
            val result = getTransactionsUseCase.execute(accountId, startDate, endDate)
                .filter { !it.isIncome }
                .map{ it.toTransactionItem() }
                .sortedByDescending { it.transactionDate }
            _expenses.value = result

            Log.d("Room", "Данные загружены из Room в количестве ${result.size}")
        }
    }
}
