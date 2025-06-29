package com.example.showmethemoney.ui.screens.account

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.domain.model.AccountHistoryDomain
import com.example.showmethemoney.R
import com.example.showmethemoney.ui.components.UniversalListItem
import com.example.showmethemoney.ui.theme.Indicator
import com.example.showmethemoney.ui.utils.StringFormatter

/**
 * Компонент для отображения основной информации о счете:
 * - Текущий баланс
 * - Валюта счета
 *
 * @param history Данные счета для отображения
 */
@Composable
fun AccountContent(history: AccountHistoryDomain) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        item {
            UniversalListItem(
                lead = "💰",
                content = "Баланс" to null,
                trail = StringFormatter.formatAmount(history.currentBalance.toDouble(),
                    history.currency) to null,
                modifier = Modifier
                    .background(Indicator)
                    .height(56.dp)
            )
        }
        item {
            UniversalListItem(
                content = "Валюта" to null,
                trail = StringFormatter.getCurrencySymbol(history.currency) to {
                    IconButton(
                        onClick = { /* TODO */ },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_more_vert),
                            contentDescription = "Подробнее",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                modifier = Modifier
                    .background(Indicator)
                    .height(56.dp)
            )
        }
    }
}