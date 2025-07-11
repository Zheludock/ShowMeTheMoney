package com.example.showmethemoney.di

import android.content.Context
import com.example.domain.repository.AccountRepository
import com.example.domain.repository.CategoriesRepository
import com.example.domain.repository.NetworkMonitor
import com.example.domain.repository.TransactionRepository
import dagger.Component

@FeatureScope
@Component(
    dependencies = [CoreComponent::class],
    modules = [RepositoryModule::class]
)
interface RepositoryComponent {
    @Component.Factory
    interface Factory {
        fun create(coreComponent: CoreComponent): RepositoryComponent
    }
    fun accountRepository(): AccountRepository
    fun categoriesRepository(): CategoriesRepository
    fun transactionRepository(): TransactionRepository
    fun context(): Context
    fun networkMonitor(): NetworkMonitor
}