package com.example.settings.pin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.settings.R

@Composable
fun PinNumpad(
    onDigitClick: (String) -> Unit,
    onBackspaceClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        listOf(
            listOf("1", "2", "3"),
            listOf("4", "5", "6"),
            listOf("7", "8", "9"),
            listOf("", "0", "⌫")
        ).forEach { row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                row.forEach { item ->
                    when (item) {
                        "⌫" -> {
                            IconButton(
                                onClick = onBackspaceClick,
                                modifier = Modifier.size(64.dp)
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_backspace),
                                    contentDescription = "Удалить"
                                )
                            }
                        }
                        "" -> {
                            Spacer(modifier = Modifier.size(64.dp))
                        }
                        else -> {
                            TextButton(
                                onClick = { onDigitClick(item) },
                                modifier = Modifier.size(64.dp),
                                shape = CircleShape
                            ) {
                                Text(
                                    text = item,
                                    style = MaterialTheme.typography.headlineMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}