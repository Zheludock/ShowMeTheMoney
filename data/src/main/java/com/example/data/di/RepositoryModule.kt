package com.example.data.di

import com.example.data.retrofit.AccountApiService
import com.example.data.retrofit.repository.AccountRepositoryImpl
import com.example.data.retrofit.CategoriesApiService
import com.example.data.retrofit.repository.CategoriesRepositoryImpl
import com.example.data.retrofit.TransactionApiService
import com.example.data.retrofit.repository.TransactionRepositoryImpl
import com.example.data.safecaller.ApiCallHelper
import com.example.domain.repository.AccountRepository
import com.example.domain.repository.CategoriesRepository
import com.example.domain.repository.TransactionRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
/**
 * Dagger-модуль для предоставления репозиториев.
 * Содержит зависимости для:
 * - AccountRepository (работа с аккаунтами)
 * - CategoriesRepository (работа с категориями)
 * - TransactionRepository (работа с транзакциями)
 *
 * @property apiService Сервис API для запросов.
 * @property apiCallHelper Вспомогательный класс для обработки вызовов API.
 */
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
