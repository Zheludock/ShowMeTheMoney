package com.example.analysis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.CategoryStatsDomain
import com.example.domain.usecase.transaction.GetTransactionsAnalysisUseCase
import com.example.utils.AccountManager
import com.example.utils.DateUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

class ExpenseAnalysisViewModel @Inject constructor(
    private val getTransactionsAnalysisUseCase: GetTransactionsAnalysisUseCase
) : ViewModel() {

    private val _startDateForUI = MutableStateFlow(DateUtils.getStartOfCurrentMonth())
    val startDateForUI: StateFlow<Date> = _startDateForUI

    private val _endDateForUI = MutableStateFlow(DateUtils.endOfDay(Date()))
    val endDateForUI: StateFlow<Date> = _endDateForUI

    private val _stats = MutableStateFlow<List<CategoryStatsDomain>>(emptyList())
    val stats: StateFlow<List<CategoryStatsDomain>> = _stats

    private val _totalSum = MutableStateFlow("0.0")
    val totalSum: StateFlow<String> = _totalSum

    private var currentJob: Job? = null

    fun updateStartDate(date: Date) {
        _startDateForUI.value = date
        reload()
    }

    fun updateEndDate(date: Date) {
        _endDateForUI.value = date
        reload()
    }

    fun loadTransactions(isIncome: Boolean) {
        currentJob?.cancel()

        val accountId = AccountManager.selectedAccountId
        val startDate = _startDateForUI.value
        val endDate = _endDateForUI.value

        currentJob = viewModelScope.launch(Dispatchers.IO) {
            getTransactionsAnalysisUseCase.execute(accountId, startDate, endDate, isIncome)
                .collect { result ->
                    val sorted = result.sortedByDescending { it.amount.toDouble() }
                    _stats.value = sorted
                    _totalSum.value = sorted.sumOf { it.amount.toDouble() }.toString()
                }
        }
    }

    private fun reload() {
        loadTransactions(isIncome = false)
    }

    override fun onCleared() {
        currentJob?.cancel()
        super.onCleared()
    }
}
