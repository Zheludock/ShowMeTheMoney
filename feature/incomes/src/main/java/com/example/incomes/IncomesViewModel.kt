package com.example.incomes

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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

class IncomesViewModel @Inject constructor(
    private val getTransactionsUseCase: GetTransactionsUseCase
): ViewModel() {
    val startDate = DateUtils.startOfDay(Date())
    val endDate = DateUtils.endOfDay(Date())

    private val _incomes = MutableStateFlow<List<TransactionItem>>(emptyList())
    val incomes: StateFlow<List<TransactionItem>> = _incomes

    fun observeTransactions() {
        viewModelScope.launch {
            getTransactionsUseCase.execute(AccountManager.selectedAccountId, startDate, endDate)
                .catch { e ->
                    // Обработка ошибок, если нужно
                    Log.e("TransactionsVM", "Error loading transactions", e)
                }
                .collect { list ->
                    _incomes.value = list.map { it.toTransactionItem() }.filter { it.isIncome }
                }
        }
    }

}
