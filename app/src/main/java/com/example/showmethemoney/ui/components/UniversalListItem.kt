package com.example.showmethemoney.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.showmethemoney.ui.theme.DividerGray
import com.example.showmethemoney.ui.theme.Indicator
import com.example.showmethemoney.ui.theme.SelectedTextUnderIcons

@Composable
fun UniversalListItem(
    lead: String? = null,
    content: Pair<String, String?>,
    trail: Pair<String?, @Composable (() -> Unit)?> = null to null,
    onClick: () -> Unit = {},
    modifier: Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .clickable { onClick() }
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Lead and content section
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                if (lead != null) {
                    Box(
                        modifier = Modifier.padding(end = 16.dp)
                            .clip(CircleShape)
                            .background(Indicator),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = lead,
                            fontSize = 24.sp,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                }

                Column(
                    modifier = Modifier.weight(1f, fill = true)
                ) {
                    Text(
                        text = content.first,
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    content.second?.let {
                        Text(
                            text = it,
                            maxLines = 1,
                            style = MaterialTheme.typography.bodyMedium,
                            overflow = TextOverflow.Ellipsis,
                            color = SelectedTextUnderIcons
                        )
                    }
                }
            }

            // Trail section
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.padding(start = 8.dp)
            ) {
                trail.first?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(end = 8.dp) // Отступ между текстом и иконкой
                    )
                }
                trail.second?.invoke()
            }
        }
        HorizontalDivider(
            thickness = 1.dp,
            color = DividerGray
        )
    }
}