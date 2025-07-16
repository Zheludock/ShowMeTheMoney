package com.example.incomes

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

class IncomesViewModel @Inject constructor(
    private val getTransactionsUseCase: GetTransactionsUseCase
): ViewModel() {
    val startDate = DateUtils.formatCurrentDate()
    val endDate = DateUtils.formatCurrentDate()

    private val _incomes = MutableStateFlow<List<TransactionItem>>(emptyList())
    val incomes: StateFlow<List<TransactionItem>> = _incomes

    fun loadIncomes(){
        viewModelScope.launch {
            val accountId = AccountManager.selectedAccountId
            val result = getTransactionsUseCase.execute(accountId, startDate, endDate)
                .filter { it.isIncome }
                .map{ it.toTransactionItem() }
                .sortedByDescending { it.createdAt }
            _incomes.value = result
        }
    }
}
