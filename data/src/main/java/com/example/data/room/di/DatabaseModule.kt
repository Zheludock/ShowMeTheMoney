package com.example.data.room.di

import android.app.Application
import androidx.room.Room
import com.example.data.room.AppDatabase
import com.example.data.room.dao.AccountDao
import com.example.data.room.dao.CategoryDao
import com.example.data.room.dao.TransactionDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            "finance.db"
        )
            .build()
    }

    @Provides
    @Singleton
    fun provideTransactionDao(db: AppDatabase): TransactionDao = db.transactionDao()

    @Provides
    @Singleton
    fun provideCategoryDao(db: AppDatabase): CategoryDao = db.categoryDao()

    @Provides
    @Singleton
    fun provideAccountDao(db: AppDatabase): AccountDao = db.accountDao()
}