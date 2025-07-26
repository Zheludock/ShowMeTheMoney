package com.example.settings.pin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ManagePinOptions(viewModel: PinViewModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Управление PIN-кодом",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Button(
            onClick = { viewModel.startChangePinProcess() },
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text("Изменить PIN-код")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.deletePin() },
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text("Удалить PIN-код")
        }
    }
}