package com.example.showmethemoney.ui.screens.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.showmethemoney.R
import com.example.showmethemoney.ui.components.UniversalListItem
import com.example.showmethemoney.ui.screens.MainViewModel
import com.example.showmethemoney.ui.theme.Indicator

@Composable
fun IncomeScreen(viewModel: MainViewModel){
    val incomeItems = viewModel.incomeItems

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        item {
            UniversalListItem(
                content = "Всего" to null,
                trail = "50000 ₽" to null,
                modifier = Modifier
                    .background(Indicator)
                    .height(56.dp)
            )
        }
        items(incomeItems) { item ->
            UniversalListItem(
                content = item.articleName to item.comment,
                trail = (item.amount + " " + item.accountCurrency) to {
                    IconButton(
                        onClick = { /* TODO: Добавить обработчик */ },
                        modifier = Modifier.size(48.dp)
                            .padding(0.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_more_vert),
                            contentDescription = "Подробнее",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            )
        }
    }
}