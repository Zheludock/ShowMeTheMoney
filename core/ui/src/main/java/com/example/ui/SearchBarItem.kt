package com.example.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.ui.theme.IconsGray

/**
 * Кастомный компонент строки поиска с возможностью очистки.
 *
 * @param searchText Текущий текст поиска
 * @param onSearchTextChanged Обработчик изменения текста поиска
 * @param modifier Modifier для кастомизации расположения и размера
 */
@Composable
fun SearchBarItem(
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
            IconButton(
                onClick = { onSearchTextChanged("") },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(painter =
                    painterResource(
                        if (searchText.isEmpty()) R.drawable.ic_find
                        else R.drawable.ic_close),
                    contentDescription =
                        if (searchText.isEmpty()) "Поиск"
                        else "Очистить",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}