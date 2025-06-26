package com.example.showmethemoney.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.showmethemoney.ui.theme.ItemGray
import com.example.showmethemoney.ui.utils.CategoryItem
/**
 * Компонент списка категорий с поиском.
 *
 * @param categories Список категорий для отображения
 */
@Composable
fun CategoryList(categories: List<CategoryItem>) {
    var searchText by remember { mutableStateOf("") }
    val filteredCategories by remember(searchText, categories) {
        derivedStateOf {
            if (searchText.isBlank()) {
                categories
            } else {
                categories.filter { category ->
                    category.name.contains(searchText, ignoreCase = true)
                }
            }
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        item {
            SearchBarItem(
                searchText = searchText,
                onSearchTextChanged = { searchText = it },
                modifier = Modifier
                    .background(ItemGray)
                    .height(56.dp)
            )
        }

        items(filteredCategories) { category ->
            UniversalListItem(
                lead = category.emoji,
                content = category.name to null,
                onClick = { /* Обработка выбора категории */ }
            )
        }
    }
}
