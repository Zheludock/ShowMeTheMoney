package com.example.data.di

import com.example.data.AccountApiService
import com.example.data.AccountRepositoryImpl
import com.example.data.CategoriesApiService
import com.example.data.CategoriesRepositoryImpl
import com.example.data.TransactionApiService
import com.example.data.TransactionRepositoryImpl
import com.example.data.safecaller.ApiCallHelper
import com.example.domain.repository.AccountRepository
import com.example.domain.repository.CategoriesRepository
import com.example.domain.repository.TransactionRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAccountRepository(
        apiService: AccountApiService,
        apiCallHelper: ApiCallHelper
    ): AccountRepository {
        return AccountRepositoryImpl(apiService, apiCallHelper)
    }

    @Provides
    @Singleton
    fun provideCategoriesRepository(
        apiService: CategoriesApiService,
        apiCallHelper: ApiCallHelper
    ): CategoriesRepository {
        return CategoriesRepositoryImpl(apiService, apiCallHelper)
    }

    @Provides
    @Singleton
    fun provideTransactionRepository(
        apiService: TransactionApiService,
        apiCallHelper: ApiCallHelper
    ): TransactionRepository {
        return TransactionRepositoryImpl(apiService, apiCallHelper)
    }
}
