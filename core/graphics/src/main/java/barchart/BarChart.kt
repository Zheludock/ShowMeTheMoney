package barchart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun BarChart(
    viewModelFactory: ViewModelProvider.Factory,
    accountId: Int
) {
    val viewModel: BarChartViewModel = viewModel(factory = viewModelFactory)

    val dataFlow = remember(accountId) { viewModel.getTransactionsLast30Days(accountId) }
    val data by dataFlow.collectAsState(initial = emptyList())

    val maxAmount = data.maxOfOrNull { kotlin.math.abs(it.totalAmount) } ?: 1.0

    val barWidth = 6.dp

    val legendDateFormatter = remember { DateTimeFormatter.ofPattern("dd.MM") }

    Column(modifier = Modifier.padding(8.dp)) {
        Row(
            modifier = Modifier
                .height(150.dp)
                .fillMaxWidth()
        ) {
            data.forEach { dayData ->
                val barHeightRatio = (kotlin.math.abs(dayData.totalAmount) / maxAmount).toFloat()
                val barHeight = 150.dp * barHeightRatio

                val barColor = if (dayData.totalAmount > 0) Color(0xFF4CAF50) else Color(0xFFF44336)

                Box(
                    modifier = Modifier
                        .width(barWidth)
                        .height(barHeight)
                        .background(barColor)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            data.forEach { dayData ->
                // Преобразуем Date в LocalDate для форматирования и получения дня месяца
                val localDate = dayData.date.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()

                val dayOfMonth = localDate.dayOfMonth

                if (dayOfMonth % 5 == 0) {
                    Text(
                        text = localDate.format(legendDateFormatter),
                        modifier = Modifier.width(barWidth),
                        maxLines = 1
                    )
                } else {
                    Spacer(modifier = Modifier.width(barWidth))
                }
            }
        }
    }
}


