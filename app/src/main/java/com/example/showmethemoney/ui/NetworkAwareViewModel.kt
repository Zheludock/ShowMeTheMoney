package com.example.showmethemoney.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.showmethemoney.data.safecaller.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class NetworkAwareViewModel @Inject constructor(
    networkMonitor: NetworkMonitor
) : ViewModel() {
    val isOnline = networkMonitor.isOnline
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )
}