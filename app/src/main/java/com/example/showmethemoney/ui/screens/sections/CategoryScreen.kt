package com.example.showmethemoney.ui.screens.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.showmethemoney.R
import com.example.showmethemoney.data.safecaller.ApiResult
import com.example.showmethemoney.ui.components.CategoryItem
import com.example.showmethemoney.ui.components.UniversalListItem
import com.example.showmethemoney.ui.theme.ItemGray

@Composable
fun CategoryScreen(viewModel: CategoryViewModel = hiltViewModel()) {
    viewModel.loadCategories()
    val categoriesState by viewModel.categories.collectAsState()

    when (val state = categoriesState) {
        is ApiResult.Loading -> LoadingIndicator()
        is ApiResult.Success -> CategoryList(state.data)
        is ApiResult.Error -> ErrorView(state.error) {
            viewModel.loadCategories()
        }
    }
}

@Composable
fun CategoryList(categories: List<CategoryItem>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        item {
            UniversalListItem(
                content = "Найти статью" to null,
                trail = null to {
                    Icon(
                        painter = painterResource(R.drawable.ic_find),
                        contentDescription = "Поиск",
                        modifier = Modifier.size(24.dp)
                    )
                },
                modifier = Modifier
                    .background(ItemGray)
                    .height(56.dp)
            )
        }
        items(categories) { category ->
            UniversalListItem(
                lead = category.emoji,
                content = category.name to null,
                onClick = { /* Обработка выбора категории */ }
            )
        }
    }
}