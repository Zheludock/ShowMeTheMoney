package com.example.showmethemoney.data

import android.content.Context
import com.example.showmethemoney.data.safecaller.AndroidNetworkMonitor
import com.example.showmethemoney.data.safecaller.ApiCallHelper
import com.example.showmethemoney.data.safecaller.NetworkMonitor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideNetworkMonitor(@ApplicationContext context: Context): NetworkMonitor {
        return AndroidNetworkMonitor(context)
    }

    @Provides
    @Singleton
    fun provideApiCallHelper(networkMonitor: NetworkMonitor): ApiCallHelper {
        return ApiCallHelper(networkMonitor)
    }

    private const val BASE_URL = "https://shmr-finance.ru/api/v1/"
    private const val TOKEN = "AALqE9czebsipeTL4BJaTxCn"

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        .create()

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

    @Provides
    @Singleton
    fun provideFinanceApiService(retrofit: Retrofit): FinanceApiService =
        retrofit.create(FinanceApiService::class.java)
}