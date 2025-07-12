package com.example.showmethemoney.di.module

import com.example.data.retrofit.AccountApiService
import com.example.data.retrofit.CategoriesApiService
import com.example.data.retrofit.TransactionApiService
import com.example.data.repository.AccountRepositoryImpl
import com.example.data.repository.CategoriesRepositoryImpl
import com.example.data.repository.TransactionRepositoryImpl
import com.example.data.safecaller.ApiCallHelper
import com.example.domain.repository.AccountRepository
import com.example.domain.repository.CategoriesRepository
import com.example.domain.repository.TransactionRepository
import com.example.showmethemoney.di.scopes.FeatureScope
import dagger.Module
import dagger.Provides

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
    @FeatureScope
    fun provideAccountRepository(
        apiService: AccountApiService,
        apiCallHelper: ApiCallHelper
    ): AccountRepository {
        return AccountRepositoryImpl(apiService, apiCallHelper)
    }

    @Provides
    @FeatureScope
    fun provideCategoriesRepository(
        apiService: CategoriesApiService,
        apiCallHelper: ApiCallHelper
    ): CategoriesRepository {
        return CategoriesRepositoryImpl(apiService, apiCallHelper)
    }

    @Provides
    @FeatureScope
    fun provideTransactionRepository(
        apiService: TransactionApiService,
        apiCallHelper: ApiCallHelper
    ): TransactionRepository {
        return TransactionRepositoryImpl(apiService, apiCallHelper)
    }
}
