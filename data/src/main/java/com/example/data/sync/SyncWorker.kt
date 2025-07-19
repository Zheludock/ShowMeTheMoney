package com.example.data.sync

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.data.repository.AccountRepositoryImpl
import com.example.data.repository.TransactionRepositoryImpl
import com.example.domain.repository.NetworkMonitor
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeoutOrNull
import java.util.concurrent.TimeUnit


class SyncWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val params: WorkerParameters,
    private val networkMonitor: NetworkMonitor,
    private val transactionRepository: TransactionRepositoryImpl,
    private val accountRepository: AccountRepositoryImpl
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            Log.d("SyncWorker", "Starting synchronization at ${System.currentTimeMillis()}")

            val hasInternet = withTimeoutOrNull(5_000L) {
                networkMonitor.isOnline.first { it }
            } ?: false

            if (!hasInternet) {
                Log.w("SyncWorker", "No internet connection, retrying later")
                return Result.retry()
            }

            transactionRepository.syncPendingTransactions()
            accountRepository.syncPendingData()

            Log.d("SyncWorker", "Synchronization completed successfully")
            Result.success()
        } catch (e: Exception) {
            Log.e("SyncWorker", "Synchronization failed", e)
            Result.retry()
        }
    }

    companion object {
        private const val WORK_NAME = "periodic_sync_work"
        const val WORK_TAG = "sync_work_tag"

        fun setup(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val syncWorkRequest = PeriodicWorkRequestBuilder<SyncWorker>(
                15,
                TimeUnit.MINUTES,
            )
                .setConstraints(constraints)
                .addTag(WORK_TAG)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.UPDATE,
                syncWorkRequest
            )

            Log.d("SyncWorker", "Periodic sync work scheduled")
        }
    }

    @AssistedFactory
    interface SyncWorkerFactory {
        fun create(context: Context, params: WorkerParameters): SyncWorker
    }
}
