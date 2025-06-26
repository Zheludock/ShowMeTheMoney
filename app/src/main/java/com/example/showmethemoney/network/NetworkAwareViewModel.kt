package com.example.showmethemoney.network

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.repository.NetworkMonitor
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
/**
 * ViewModel, предоставляющая состояние подключения к сети.
 * Реализуется в MainScreen, из которого вызываются все остальные экраны.
 *
 * @property isOnline [Flow] с булевым значением: `true` если устройство онлайн.
 *
 * @param networkMonitor Зависимость для мониторинга состояния сети (внедряется через DI).
 */
class NetworkAwareViewModel @Inject constructor(
    networkMonitor: NetworkMonitor,
) : ViewModel() {
    val isOnline = networkMonitor.isOnline
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )
}