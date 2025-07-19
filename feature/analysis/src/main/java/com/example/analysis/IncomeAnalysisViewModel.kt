package com.example.analysis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.CategoryStatsDomain
import com.example.domain.usecase.transaction.GetTransactionsAnalysisUseCase
import com.example.utils.AccountManager
import com.example.utils.DateUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class IncomeAnalysisViewModel @Inject constructor(
    private val getTransactionsAnalysisUseCase: GetTransactionsAnalysisUseCase
): ViewModel() {
    private val _startDateForUI = MutableStateFlow(DateUtils.getFirstDayOfCurrentMonth())
    val startDateForUI: StateFlow<String> = _startDateForUI

    private val _endDateForUI = MutableStateFlow(DateUtils.formatCurrentDate())
    val endDateForUI: StateFlow<String> = _endDateForUI

    private val _stats = MutableStateFlow<List<CategoryStatsDomain>>(emptyList())
    val stats: StateFlow<List<CategoryStatsDomain>> = _stats

    private val _totalSum = MutableStateFlow("0.0")
    val totalSum: StateFlow<String> = _totalSum

    fun updateStartDate(date: String) {
        _startDateForUI.value = date
    }

    fun updateEndDate(date: String) {
        _endDateForUI.value = date
    }

    fun loadTransactions(startDate: String = startDateForUI.value,
                         endDate: String = endDateForUI.value,
                         isIncome: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val accountId = AccountManager.selectedAccountId
            val result = getTransactionsAnalysisUseCase.execute(accountId, startDate, endDate, isIncome)
                .sortedByDescending { it.amount.toDouble() }
            _stats.value = result
            _totalSum.value = result.sumOf { it.amount.toDouble() }.toString()
        }
    }
}