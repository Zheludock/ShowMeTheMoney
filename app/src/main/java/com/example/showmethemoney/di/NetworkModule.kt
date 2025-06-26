package com.example.showmethemoney.di

import com.example.domain.repository.NetworkMonitor
import com.example.showmethemoney.network.AndroidNetworkMonitor
import dagger.Binds
import dagger.Module

@Module
abstract class NetworkModule {
    @Binds
    abstract fun bindNetworkMonitor(
        monitor: AndroidNetworkMonitor
    ): NetworkMonitor
}

