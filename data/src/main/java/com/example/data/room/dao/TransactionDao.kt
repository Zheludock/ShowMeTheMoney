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
    @Query("""
    SELECT * FROM transactions
    WHERE accountId = :accountId
    AND transactionDate >= :startDate
    AND transactionDate <= :endDate
    """)
    suspend fun getTransactions(accountId: Int,
                                startDate: String?,
                                endDate: String?): List<TransactionWithCategoryAndAccount>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactions(transactions: List<TransactionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Query("DELETE FROM transactions WHERE id = :transactionId")
    suspend fun deleteTransaction(transactionId: Int)

    @Transaction
    @Query("SELECT * FROM transactions WHERE id = :transactionId")
    suspend fun getTransactionWithDetails(transactionId: Int): TransactionWithCategoryAndAccount?
}