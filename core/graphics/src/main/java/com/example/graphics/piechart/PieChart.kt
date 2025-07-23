package com.example.graphics.piechart


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import java.util.Date

@Composable
fun PieChart(
    accountId: Int,
    startDate: Date,
    endDate: Date,
    isIncome: Boolean,
    viewModelFactory: ViewModelProvider.Factory,
) {
    val viewModel: PieChartViewModel = viewModel(factory = viewModelFactory)
    viewModel.setParams(accountId, startDate, endDate, isIncome)

    val entries by viewModel.data.collectAsState()

    if (entries.isEmpty()) return

    val total = entries.sumOf { it.value.toDouble() }.toFloat()
    val colors = listOf(
        Color(0xFFEF5350),
        Color(0xFFAB47BC),
        Color(0xFF42A5F5),
        Color(0xFF26A69A),
        Color(0xFFFFCA28),
        Color(0xFFFF7043),
        Color(0xFF8D6E63),
        Color(0xFF789262),
    )

    val gapAngle = 2f

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Box(
                modifier = Modifier.size(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.matchParentSize()) {
                    val canvasSize = size.minDimension
                    val radius = canvasSize / 2

                    var startAngle = -90f

                    entries.forEachIndexed { index, entry ->
                        val sweepAngle = (entry.value / total) * 360f - gapAngle
                        val color = colors[index % colors.size]

                        if (sweepAngle > 0) {
                            drawArc(
                                color = color,
                                startAngle = startAngle,
                                sweepAngle = sweepAngle,
                                useCenter = false,
                                topLeft = Offset(
                                    (size.width - 2 * radius) / 2,
                                    (size.height - 2 * radius) / 2
                                ),
                                size = Size(2 * radius, 2 * radius),
                                style = Stroke(width = 15F, cap = StrokeCap.Butt)
                            )
                        }

                        startAngle += sweepAngle + gapAngle
                    }
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .width(120.dp)
                        .heightIn(max = 140.dp)
                ) {
                    entries.forEachIndexed { index, entry ->
                        val color = colors[index % colors.size]
                        val percent = (entry.value / total) * 100

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .background(color = color, shape = RoundedCornerShape(12.dp))
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = entry.name,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontSize = 7.sp,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "%.1f%%".format(percent),
                                fontWeight = FontWeight.Bold,
                                fontSize = 7.sp
                            )
                        }
                    }
                }
            }
        }
    }
}