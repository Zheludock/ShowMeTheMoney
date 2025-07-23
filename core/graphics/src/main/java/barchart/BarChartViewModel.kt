package barchart

import androidx.lifecycle.ViewModel
import com.example.domain.usecase.transaction.GetTransactionsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import javax.inject.Inject

class BarChartViewModel @Inject constructor(
    private val getTransactionsUseCase: GetTransactionsUseCase
) : ViewModel() {

    fun getTransactionsLast30Days(accountId: Int): Flow<List<DailyTransactionSum>> {
        val endDate = LocalDate.now()
        val startDate = endDate.minusDays(29)

        val startDateDate = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
        val endDateDate = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant())

        return getTransactionsUseCase.execute(accountId, startDateDate, endDateDate)
            .map { transactions ->
                val dailySumsMap: Map<LocalDate, Double> = transactions.groupBy { transaction ->
                    transaction.transactionDate.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                }.mapValues { entry ->
                    entry.value.sumOf { it.amount.toDoubleOrNull() ?: 0.0 }
                }

                val result = mutableListOf<DailyTransactionSum>()
                var currentDate = startDate

                for (i in 0..29) {
                    val sum = dailySumsMap[currentDate] ?: 0.0
                    val dateAsDate = Date.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
                    result.add(DailyTransactionSum(dateAsDate, sum))
                    currentDate = currentDate.plusDays(1)
                }
                result
            }
    }
}
