package com.example.showmethemoney.di

import android.content.Context
import com.example.showmethemoney.data.safecaller.AndroidNetworkMonitor
import com.example.showmethemoney.data.safecaller.NetworkMonitor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideNetworkMonitor(@ApplicationContext context: Context): NetworkMonitor {
        return AndroidNetworkMonitor(context)
    }
}