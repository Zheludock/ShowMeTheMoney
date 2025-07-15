package com.example.showmethemoney.di.component

import android.app.Application
import android.content.Context
import com.example.data.retrofit.AccountApiService
import com.example.data.retrofit.CategoriesApiService
import com.example.data.retrofit.TransactionApiService
import com.example.data.room.dao.AccountDao
import com.example.data.room.dao.CategoryDao
import com.example.data.room.dao.TransactionDao
import com.example.data.room.di.DatabaseModule
import com.example.data.safecaller.ApiCallHelper
import com.example.domain.repository.NetworkMonitor
import com.example.showmethemoney.di.module.AppModule
import com.example.showmethemoney.di.module.NetworkModule
import com.example.showmethemoney.di.module.NetworkMonitorModule
import com.example.showmethemoney.di.module.SafeApiModule
import dagger.BindsInstance
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    NetworkModule::class,
    SafeApiModule::class,
    NetworkMonitorModule::class,
    DatabaseModule::class
])
interface CoreComponent {
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Application
        ): CoreComponent
    }

    fun application(): Application
    fun context(): Context
    fun retrofit(): Retrofit
    fun apiCallHelper(): ApiCallHelper
    fun accountApiService(): AccountApiService
    fun categoriesApiService(): CategoriesApiService
    fun transactionApiService(): TransactionApiService
    fun networkMonitor(): NetworkMonitor
    fun accountDao(): AccountDao
    fun transactionDao(): TransactionDao
    fun categoryDao(): CategoryDao
}