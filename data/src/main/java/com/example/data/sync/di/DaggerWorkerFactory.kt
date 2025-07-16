package com.example.data.sync.di

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.data.repository.AccountRepositoryImpl
import com.example.data.repository.TransactionRepositoryImpl
import com.example.data.sync.SyncWorker
import javax.inject.Inject

class DaggerWorkerFactory @Inject constructor(
    private val transactionRepository: TransactionRepositoryImpl,
    private val accountRepository: AccountRepositoryImpl
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when(workerClassName) {
            SyncWorker::class.java.name -> SyncWorker(
                appContext,
                workerParameters,
                transactionRepository,
                accountRepository,
            )
            else -> null
        }
    }
}