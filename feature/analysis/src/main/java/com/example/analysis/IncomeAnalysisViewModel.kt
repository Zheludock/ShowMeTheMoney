package com.example.analysis

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.CategoryStatsDomain
import com.example.domain.usecase.transaction.GetTransactionsAnalysisUseCase
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
class IncomeAnalysisViewModel @Inject constructor(
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

    fun updateStartDate(date: Date) {
        _startDateForUI.value = date
    }

    fun updateEndDate(date: Date) {
        _endDateForUI.value = date
    }

    init {
        viewModelScope.launch {
            combine(_startDateForUI, _endDateForUI){ startDate, endDate ->
                startDate to endDate
            }
                .flatMapLatest { (startDate, endDate) ->
                    getTransactionsAnalysisUseCase.execute(AccountManager.selectedAccountId, startDate, endDate, true)
                        .catch { e ->
                            Log.e("TransactionsAnalysisVM", "Error loading transactions", e)
                            emit(emptyList()) // в случае ошибки возвращаем пустой список
                        }
                }
                .collect { list ->
                    _stats.value = list.sortedByDescending { it.amount.toDouble() }
                    _totalSum.value = list.sumOf { it.amount.toDouble() }.toString()
                }
        }
    }
}