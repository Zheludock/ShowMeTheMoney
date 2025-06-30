package com.example.showmethemoney.ui.screens.account

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun EditAccountNameDialog(
    initialName: String,
    onDismissRequest: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var accountName by remember { mutableStateOf(initialName) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = "Новое название счета"
            )
        },
        text = {
            OutlinedTextField(
                value = accountName,
                onValueChange = { accountName = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = { Text("Введите название") }
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(accountName) },
                enabled = accountName.isNotBlank()
            ) {
                Text("ОК")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Отмена")
            }
        },
        shape = MaterialTheme.shapes.extraLarge
    )
}