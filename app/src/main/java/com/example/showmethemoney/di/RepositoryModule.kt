package com.example.showmethemoney.di

import com.example.showmethemoney.data.FinanceApiService
import com.example.showmethemoney.data.FinanceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideFinanceRepository(apiService: FinanceApiService): FinanceRepository {
        return FinanceRepository(apiService)
    }
}
