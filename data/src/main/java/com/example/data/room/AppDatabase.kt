package com.example.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.room.dao.AccountDao
import com.example.data.room.dao.CategoryDao
import com.example.data.room.dao.TransactionDao
import com.example.data.room.entityes.AccountDetailsEntity
import com.example.data.room.entityes.AccountEntity
import com.example.data.room.entityes.AccountHistoryEntity
import com.example.data.room.entityes.AccountHistoryItemEntity
import com.example.data.room.entityes.CategoryEntity
import com.example.data.room.entityes.CategoryStatsEntity
import com.example.data.room.entityes.TransactionEntity

@Database(
    entities = [
        TransactionEntity::class,
        CategoryEntity::class,
        AccountEntity::class,
        AccountHistoryEntity::class,
        AccountDetailsEntity::class,
        AccountHistoryItemEntity::class,
        CategoryStatsEntity::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao
    abstract fun accountDao(): AccountDao
}