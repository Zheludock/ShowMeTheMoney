package com.example.showmethemoney.ui.screens.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import com.example.showmethemoney.ui.theme.ItemGray

@Composable
fun CategoryScreen(viewModel: MainViewModel) {
    val categoryItems = viewModel.categoryItems

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        item {
            UniversalListItem(
                content = "Найти статью" to null,
                trail = null to {
                    IconButton(
                        onClick = { /* TODO: Добавить обработчик */ },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_find),
                            contentDescription = "Поиск",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                modifier = Modifier
                    .background(ItemGray)
                    .height(56.dp)
            )
        }
        items(categoryItems) { item ->
            UniversalListItem(
                lead = item.emoji,
                content = item.name to null
            )
        }
    }
}