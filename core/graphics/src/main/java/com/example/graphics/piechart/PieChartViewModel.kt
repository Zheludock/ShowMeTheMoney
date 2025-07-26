package com.example.graphics.piechart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.transaction.GetTransactionsAnalysisUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.Date
import javax.inject.Inject

class PieChartViewModel @Inject constructor(
    private val getTransactionsAnalysisUseCase: GetTransactionsAnalysisUseCase
) : ViewModel() {

    private val _accountId = MutableStateFlow<Int?>(null)
    private val _startDate = MutableStateFlow<Date?>(null)
    private val _endDate = MutableStateFlow<Date?>(null)
    private val _isIncome = MutableStateFlow<Boolean?>(null)

    private val paramsFlow = combine(_accountId, _startDate, _endDate, _isIncome)
    { accountId, startDate, endDate, isIncome ->
        Quadruple(accountId, startDate, endDate, isIncome)
    }.filter { (accountId, startDate, endDate, isIncome) ->
        accountId != null && startDate != null && endDate != null && isIncome != null
    }.map { (accountId, startDate, endDate, isIncome) ->
        Quadruple(accountId!!, startDate!!, endDate!!, isIncome!!)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val data: StateFlow<List<PieChartEntry>> = paramsFlow
        .flatMapLatest { (accountId, startDate, endDate, isIncome) ->
            getTransactionsAnalysisUseCase.execute(
                accountId = accountId,
                startDate = startDate,
                endDate = endDate,
                isIncome = isIncome
            ).map { list ->
                list.mapNotNull { domain ->
                    val amountFloat = domain.amount.replace(",", ".").toFloatOrNull()
                    if (amountFloat != null) PieChartEntry(domain.categoryName, amountFloat) else null
                }
                    .sortedByDescending { it.value } // сортируем по убыванию суммы
                    .let { sortedList ->
                        if (sortedList.size <= 5) {
                            sortedList
                        } else {
                            val topFive = sortedList.take(5)
                            val othersSum = sortedList.drop(5).sumOf { it.value.toDouble() }
                            topFive + PieChartEntry("Иное", othersSum.toFloat())
                        }
                    }
            }
        }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun setParams(accountId: Int, startDate: Date, endDate: Date, isIncome: Boolean) {
        _accountId.value = accountId
        _startDate.value = startDate
        _endDate.value = endDate
        _isIncome.value = isIncome
    }

    private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
}

