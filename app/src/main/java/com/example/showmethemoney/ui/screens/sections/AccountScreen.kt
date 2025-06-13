package com.example.showmethemoney.ui.screens.sections

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
import com.example.showmethemoney.R
import com.example.showmethemoney.ui.components.UniversalListItem
import com.example.showmethemoney.ui.theme.Indicator

@Composable
fun AccountScreen(){
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        item {
            UniversalListItem(
                lead = "💰",
                content = "Баланс" to null,
                trail = "500000 ₽" to null,
                modifier = Modifier
                    .background(Indicator)
                    .height(56.dp)
            )
        }
        item {
            UniversalListItem(
                content = "Валюта" to null,
                trail = "₽" to {
                    IconButton(
                        onClick = { /* TODO: Добавить обработчик */ },
                        modifier = Modifier.size(48.dp)
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