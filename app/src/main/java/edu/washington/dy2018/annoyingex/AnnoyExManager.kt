package edu.washington.dy2018.annoyingex

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

class AnnoyExManager(context: Context) {
    private var workManager = WorkManager.getInstance(context)

    fun startAnnoyingEx() {
        if (isAXRunning()) {
            stopWork()
        }

        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .build()

        //Todo: remove the commented line ".setConstraints(constraints)" here
        val workRequest = PeriodicWorkRequestBuilder<PostNotificationWorker>(20, TimeUnit.MINUTES)
            .setInitialDelay(5, TimeUnit.SECONDS)
            // .setConstraints(constraints)
            .addTag(AX_WORK_REQUEST_TAG)
            .build()

        workManager.enqueue(workRequest)
        startFetching()
    }

    private fun isAXRunning(): Boolean {
        return when (workManager.getWorkInfosByTag(AX_WORK_REQUEST_TAG).get().firstOrNull()?.state) {
            WorkInfo.State.RUNNING,
                WorkInfo.State.ENQUEUED -> true
            else -> false
        }
    }

    fun stopWork() {
        workManager.cancelAllWorkByTag(AX_WORK_REQUEST_TAG)
        workManager.cancelAllWorkByTag(FETCH_WORK_REQUEST_TAG)
    }

    // Extra Credit 2
    private fun startFetching() {
        if (isFetchRunning()) {
            stopFetchWork()
        }

        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<FetchDataWorker>(2, TimeUnit.DAYS)
            .setConstraints(constraints)
            .addTag(FETCH_WORK_REQUEST_TAG)
            .build()

        workManager.enqueue(workRequest)
    }

    private fun isFetchRunning(): Boolean {
        return when (workManager.getWorkInfosByTag(FETCH_WORK_REQUEST_TAG).get().firstOrNull()?.state) {
            WorkInfo.State.RUNNING,
            WorkInfo.State.ENQUEUED -> true
            else -> false
        }
    }

    private fun stopFetchWork() {
        workManager.cancelAllWorkByTag(FETCH_WORK_REQUEST_TAG)
    }

    companion object {
        const val AX_WORK_REQUEST_TAG = "AX_WORK_REQUEST_TAG"
        const val FETCH_WORK_REQUEST_TAG = "FETCH_WORK_REQUEST_TAG"
    }
}