package com.example.showmethemoney.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.showmethemoney.navigation.BottomNavItem
import com.example.showmethemoney.ui.theme.AppFontFamily
import com.example.showmethemoney.ui.theme.BackgroundBottomBar
import com.example.showmethemoney.ui.theme.IconsGray
import com.example.showmethemoney.ui.theme.IconsGreen
import com.example.showmethemoney.ui.theme.Indicator
import com.example.showmethemoney.ui.theme.SelectedTextUnderIcons
import com.example.showmethemoney.ui.theme.TextUnderIcons

@Composable
fun AppBottomNavigation(
    navItems: List<BottomNavItem>,
    currentRoute: String,
    onItemClick: (String) -> Unit
) {
    NavigationBar(
        modifier = Modifier.background(color = BackgroundBottomBar)
            .padding(0.dp)
            .fillMaxWidth(),
        containerColor = BackgroundBottomBar // Цвет фона всей панели
    ) {
        navItems.forEach { item ->
            val selected = currentRoute == item.route

            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.label,
                        tint = if (selected) {
                            IconsGreen
                        } else {
                            IconsGray
                        }
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        style = if (selected) { TextStyle(fontWeight = FontWeight.Medium,
                            color = SelectedTextUnderIcons)}
                        else {TextStyle(fontWeight = FontWeight.Normal,
                            color = SelectedTextUnderIcons)},
                        fontSize = 12.sp,
                        fontFamily = AppFontFamily,
                        maxLines = 1,
                    )
                },
                selected = selected,
                onClick = { onItemClick(item.route) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = IconsGreen, // Дублирование для совместимости
                    unselectedIconColor = IconsGray,
                    selectedTextColor = SelectedTextUnderIcons,
                    unselectedTextColor = TextUnderIcons,
                    indicatorColor = Indicator
                )
            )
        }
    }
}