package com.example.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.data.room.entityes.AccountDetailsEntity
import com.example.data.room.entityes.AccountDetailsWithStats
import com.example.data.room.entityes.AccountEntity
import com.example.data.room.entityes.AccountHistoryEntity
import com.example.data.room.entityes.AccountHistoryItemEntity
import com.example.data.room.entityes.AccountHistoryWithItems
import com.example.data.room.entityes.CategoryStatsEntity

@Dao
interface AccountDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccounts(accounts: List<AccountEntity>)

    @Query("SELECT * FROM accounts")
    suspend fun getAllAccounts(): List<AccountEntity>

    @Query("SELECT * FROM accounts WHERE id = :id")
    suspend fun getAccountById(id: Int): AccountEntity

    @Update
    suspend fun updateAccount(account: AccountEntity)

    @Transaction
    @Query("SELECT * FROM account_history WHERE accountId = :accountId")
    suspend fun getAccountHistory(accountId: Int): AccountHistoryWithItems?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccountHistory(history: AccountHistoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccountHistoryItems(items: List<AccountHistoryItemEntity>)

    @Transaction
    suspend fun insertFullHistory(history: AccountHistoryEntity, items: List<AccountHistoryItemEntity>) {
        insertAccountHistory(history)
        insertAccountHistoryItems(items)
    }

    @Transaction
    @Query("SELECT * FROM account_details WHERE id = :accountId")
    suspend fun getAccountDetails(accountId: Int): AccountDetailsWithStats?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccountDetails(details: AccountDetailsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategoryStats(stats: List<CategoryStatsEntity>)

    @Transaction
    suspend fun insertFullDetails(details: AccountDetailsEntity, stats: List<CategoryStatsEntity>) {
        insertAccountDetails(details)
        insertCategoryStats(stats)
    }
}