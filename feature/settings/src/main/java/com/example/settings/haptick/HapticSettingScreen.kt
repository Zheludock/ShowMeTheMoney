package com.example.settings.haptick

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.utils.TopBarState

@Composable
fun HapticSettingsScreen (
    hapticFeedbackManager: HapticFeedbackManager,
    preferencesManager: HaptickPreferenceManager,
    updateTopBar: (TopBarState) -> Unit,
) {
    LaunchedEffect(Unit) {
        updateTopBar(
            TopBarState(
                title = "Хаптики"
            )
        )
    }

    var hapticEnabled by remember { mutableStateOf(preferencesManager.isHapticEnabled()) }
    var selectedEffect by remember { mutableStateOf(preferencesManager.getSelectedHapticEffect()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        SwitchSetting(
            title = "Отклик при нажатии",
            checked = hapticEnabled,
            onCheckedChange = {
                hapticEnabled = it
                preferencesManager.setHapticEnabled(it)
            }
        )

        if (hapticEnabled) {
            Text(
                text = "Выберите эффект при нажатии",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            HapticEffect.entries.forEach { effect ->
                RadioButtonSetting(
                    text = effect.name.replace("_", " "),
                    selected = selectedEffect == effect,
                    onSelect = {
                        selectedEffect = effect
                        preferencesManager.setSelectedHapticEffect(effect)
                        hapticFeedbackManager.performClickFeedback()
                    }
                )
            }
        }
    }
}