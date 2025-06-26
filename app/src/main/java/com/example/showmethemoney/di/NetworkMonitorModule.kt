package com.example.showmethemoney.di

import com.example.domain.repository.NetworkMonitor
import com.example.showmethemoney.network.AndroidNetworkMonitor
import dagger.Binds
import dagger.Module
/**
 * Dagger-модуль для предоставления зависимости [NetworkMonitor].
 *
 * Связывает абстракцию [NetworkMonitor] с реализацией [AndroidNetworkMonitor].
 * Это позволяет внедрять [NetworkMonitor] в другие классы, сохраняя гибкость
 * для замены реализации (например, на mock-объект в тестах).
 *
 * @see Binds для связывания интерфейса с реализацией без явного создания экземпляра.
 */
@Module
abstract class NetworkMonitorModule {
    /**
     * Связывает [NetworkMonitor] с [AndroidNetworkMonitor].
     *
     * @param monitor Реализация [AndroidNetworkMonitor], которую предоставляет Dagger.
     * @return Абстракция [NetworkMonitor] для внедрения в другие классы.
     */
    @Binds
    abstract fun bindNetworkMonitor(
        monitor: AndroidNetworkMonitor
    ): NetworkMonitor
}

