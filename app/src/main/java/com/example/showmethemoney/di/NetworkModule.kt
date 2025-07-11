package com.example.showmethemoney.di

import com.example.data.retrofit.AccountApiService
import com.example.data.retrofit.CategoriesApiService
import com.example.data.retrofit.TransactionApiService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Модуль Dependency Injection для настройки сетевого слоя приложения.
 * Предоставляет зависимости для работы с API: Retrofit, OkHttp, Gson и API-сервисы.
 */
@Module
object NetworkModule {
    private const val BASE_URL = "https://shmr-finance.ru/api/v1/"
    private const val TOKEN = "AALqE9czebsipeTL4BJaTxCn"
    private const val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

    /**
     * Предоставляет настроенный экземпляр [Gson] для сериализации/десериализации JSON.
     * Настройки:
     * - Формат даты в соответствии с API
     */
    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder()
        .setDateFormat(DATE_FORMAT)
        .create()
    /**
     * Предоставляет настроенный экземпляр [OkHttpClient] с:
     * - Авторизационным токеном
     * - Логированием запросов/ответов
     * - Интерсепторами
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $TOKEN")
                .build()
            chain.proceed(request)
        }
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()
    /**
     * Предоставляет экземпляр [Retrofit] с базовыми настройками:
     * - Базовый URL API
     * - Клиент OkHttp
     * - Gson-конвертер
     */
    @Provides
    @Singleton
    fun provideRetrofit(
        gson: Gson,
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
    /**
     * Предоставляет экземпляр [CategoriesApiService]
     */
    @Provides
    @Singleton
    fun provideCategoriesApiService(retrofit: Retrofit): CategoriesApiService =
        retrofit.create(CategoriesApiService::class.java)
    /**
     * Предоставляет экземпляр [TransactionApiService]
     */
    @Provides
    @Singleton
    fun provideTransactionApiService(retrofit: Retrofit): TransactionApiService =
        retrofit.create(TransactionApiService::class.java)
    /**
     * Предоставляет экземпляр [AccountApiService]
     */
    @Provides
    @Singleton
    fun provideAccountApiService(retrofit: Retrofit): AccountApiService =
        retrofit.create(AccountApiService::class.java)
}