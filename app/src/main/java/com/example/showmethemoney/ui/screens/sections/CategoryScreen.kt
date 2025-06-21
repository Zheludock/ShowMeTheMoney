package com.example.showmethemoney.ui.screens.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsEndWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.showmethemoney.R
import com.example.showmethemoney.data.safecaller.ApiResult
import com.example.showmethemoney.ui.utils.CategoryItem
import com.example.showmethemoney.ui.components.ErrorView
import com.example.showmethemoney.ui.components.LoadingIndicator
import com.example.showmethemoney.ui.components.UniversalListItem
import com.example.showmethemoney.ui.theme.IconsGray
import com.example.showmethemoney.ui.theme.ItemGray

@Composable
fun CategoryScreen(viewModel: CategoryViewModel = hiltViewModel()) {
    LaunchedEffect(Unit) {
        viewModel.loadCategories()
    }
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

@Composable
private fun SearchBarItem(
    searchText: String,
    onSearchTextChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = searchText,
            onValueChange = onSearchTextChanged,
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(color = LocalContentColor.current),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier,
                ) {
                    if (searchText.isEmpty()) {
                        Text(
                            text = "Найти статью",
                            color = IconsGray
                        )
                    }
                    innerTextField()
                }
            }
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 4.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            if (searchText.isNotEmpty()) {
                IconButton(
                    onClick = { onSearchTextChanged("") },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_close),
                        contentDescription = "Очистить",
                        modifier = Modifier.size(24.dp)
                    )
                }
            } else {
                Icon(
                    painter = painterResource(R.drawable.ic_find),
                    contentDescription = "Поиск",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
