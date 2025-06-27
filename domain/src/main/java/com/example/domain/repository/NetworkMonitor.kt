package com.example.domain.repository

import kotlinx.coroutines.flow.Flow
/**
 * Интерфейс для мониторинга состояния сетевого подключения.
 *
 * Предоставляет поток данных (Flow) с текущим состоянием подключения:
 * - true: устройство онлайн
 * - false: устройство оффлайн
 */
interface NetworkMonitor {
    val isOnline: Flow<Boolean>
}