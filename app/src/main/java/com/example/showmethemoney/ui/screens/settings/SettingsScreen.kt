package com.example.showmethemoney.ui.screens.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.domain.model.listSettings
import com.example.showmethemoney.R
import com.example.showmethemoney.ui.components.AppTopBar
import com.example.showmethemoney.ui.components.UniversalListItem

/**
 * Экран настроек приложения.
 *
 * Отображает список настроек, включая переключатель для темы
 * и кнопки навигации (например, "О приложении").
 *
 * @param listSettings Список настроек для отображения.
 */
@Composable
fun SettingsScreen() {
    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            AppTopBar(title = stringResource(R.string.settings))
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(listSettings) { item ->
                    UniversalListItem(
                        content = item.title to null,
                        trail = if (item.isSwitch) {
                            null to {
                                Switch(
                                    checked = false,
                                    onCheckedChange = { /* Изменение темы будет здесь */ },
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                            }
                        } else {
                            null to {
                                IconButton(
                                    onClick = { /* TODO */ },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_arrow_right),
                                        contentDescription = "Подробнее",
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        },
                        modifier = Modifier.height(56.dp)
                    )
                }
            }
        }
    }
}