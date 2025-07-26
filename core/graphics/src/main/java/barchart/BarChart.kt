package barchart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
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

    val legendDateFormatter = remember { DateTimeFormatter.ofPattern("dd.MM") }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    Column(modifier = Modifier.padding(16.dp)) {
        val barCount = data.size
        val availableWidth = screenWidth - 32.dp
        val barWidth = if (barCount > 0) {
            val totalSpacerWidth = (barCount - 1) * 2.dp
            (availableWidth - totalSpacerWidth) / barCount
        } else {
            0.dp
        }

        Box(
            modifier = Modifier
                .height(150.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                data.forEach { dayData ->
                    val barHeightRatio = (kotlin.math.abs(dayData.totalAmount) / maxAmount).toFloat()
                    val barHeight = 150.dp * barHeightRatio
                    val barColor = if (dayData.totalAmount > 0) Color(0xFF4CAF50) else Color(0xFFF44336)

                    Column(
                        modifier = Modifier
                            .width(barWidth)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Box(
                            modifier = Modifier
                                .width(barWidth)
                                .height(barHeight)
                                .background(barColor)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            data.forEach { dayData ->
                val localDate = dayData.date.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()

                val dayOfMonth = localDate.dayOfMonth
                val targetDays = listOf(1, 5, 10, 15, 20, 25)

                if (dayOfMonth in targetDays) {
                    Box(
                        modifier = Modifier.width(barWidth * 1.5f)
                    ) {
                        Text(
                            text = localDate.format(legendDateFormatter),
                            modifier = Modifier
                                .rotate(90f)
                                .widthIn(min = barWidth * 1.5f)
                                .align(Alignment.CenterStart),
                            fontSize = 10.sp,
                            softWrap = false,
                            overflow = TextOverflow.Visible
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.width(barWidth))
                }
            }
        }
    }
}