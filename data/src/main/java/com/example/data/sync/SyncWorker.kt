package com.example.data.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.data.repository.AccountRepositoryImpl
import com.example.data.repository.TransactionRepositoryImpl


class SyncWorker(
    context: Context,
    params: WorkerParameters,
    private val transactionRepository: TransactionRepositoryImpl,
    private val accountRepository: AccountRepositoryImpl,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            transactionRepository.syncPendingTransactions()
            accountRepository.syncPendingData()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
