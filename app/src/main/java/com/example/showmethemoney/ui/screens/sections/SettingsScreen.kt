package com.example.showmethemoney.ui.screens.sections

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.showmethemoney.R
import com.example.domain.model.listSettins
import com.example.showmethemoney.ui.components.UniversalListItem

@Composable
fun SettingsScreen() {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(listSettins) { item ->
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