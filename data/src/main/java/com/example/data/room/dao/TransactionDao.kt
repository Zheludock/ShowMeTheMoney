package com.example.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.data.room.entityes.TransactionEntity
import com.example.data.room.entityes.TransactionWithCategoryAndAccount

@Dao
interface TransactionDao {
    @Transaction
    @Query("SELECT * FROM transactions WHERE accountId = :accountId AND (transactionDate >= :startDate AND transactionDate <= :endDate)")
    suspend fun getTransactions(accountId: Int,
                                startDate: String?,
                                endDate: String?): List<TransactionWithCategoryAndAccount>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactions(transactions: List<TransactionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionById(id: Int): TransactionEntity

    @Query("DELETE FROM transactions WHERE id = :transactionId")
    suspend fun deleteTransaction(transactionId: Int)

    @Transaction
    @Query("SELECT * FROM transactions WHERE id = :transactionId")
    suspend fun getTransactionWithDetails(transactionId: Int): TransactionWithCategoryAndAccount?

    @Query("UPDATE transactions SET isDeleted = 1 WHERE id = :transactionId")
    suspend fun markTransactionDeleted(transactionId: Int)

    @Query("SELECT * FROM transactions WHERE pendingSync = 1")
    fun getPendingSyncTransactions(): List<TransactionEntity>

    @Query("SELECT COUNT(*) FROM transactions WHERE accountId = :accountId AND isDeleted = 0")
    suspend fun transactionsCountByAccount(accountId: Int): Int

    @Query("SELECT COUNT(*) FROM transactions WHERE accountId = :accountId AND (:startDate IS NULL OR :endDate IS NULL OR transactionDate BETWEEN :startDate AND :endDate)")
    suspend fun transactionsCountByAccountAndDate(accountId: Int, startDate: String?, endDate: String?): Int

    @Query("SELECT * FROM transactions")
    suspend fun getFirstTransaction(): List<TransactionWithCategoryAndAccount>
}