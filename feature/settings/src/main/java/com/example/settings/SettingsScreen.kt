package com.example.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ui.UniversalListItem
import com.example.utils.ThemeManager
import com.example.utils.TopBarState

/**
 * Экран настроек приложения.
 *
 * Отображает список настроек, включая переключатель для темы
 * и кнопки навигации (например, "О приложении").
 *
 * @param listSettings Список настроек для отображения.
 */
@Composable
fun SettingsScreen(
    updateTopBar: (TopBarState) -> Unit,
    navController: NavController,
) {

    val context = LocalContext.current

    var isDarkTheme by remember {
        mutableStateOf(ThemeManager.isDarkThemeEnabled(context))
    }

    var selectedColorScheme by remember {
        mutableStateOf(ThemeManager.getSelectedColorScheme(context))
    }

    LaunchedEffect(Unit) {
        updateTopBar(
            TopBarState(
                title = "Настройки"
            )
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(listSettings) { item ->
            UniversalListItem(
                content = item.title to null,
                trail = if (item.isSwitch) {
                    null to {
                        Switch(
                            checked = isDarkTheme,
                            onCheckedChange = { ThemeManager.setDarkThemeEnabled(context, it)
                                isDarkTheme = it },
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                } else {
                    null to {
                        Icon(
                            painter = painterResource(com.example.ui.R.drawable.ic_arrow_right),
                            contentDescription = "Подробнее",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                modifier = Modifier.height(56.dp),
                onClick = { when (item.name) {
                    "mainColor" ->  navController.navigate("colorChange")
                    "about" -> navController.navigate("about")
                    "code" -> navController.navigate("pin")
                    else -> {}
                }}
            )
        }
    }
}
