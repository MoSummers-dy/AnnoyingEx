package edu.washington.dy2018.annoyingex

import android.content.Context
import android.util.Log
import androidx.work.*
import java.util.concurrent.TimeUnit

class AnnoyExManager(private val context: Context) {
    private var workManager = WorkManager.getInstance(context)

    fun startAnnoyingEx() {
        if (isAXRunning()) {
            stopWork()
        }

        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .build()

        //Todo: remove the commented lines here; it is only for testing
        val workRequest = OneTimeWorkRequestBuilder<PostNotificationWorker>()
            .setInitialDelay(5, TimeUnit.SECONDS)
            // .setConstraints(constraints)
            .addTag(AX_WORK_REQUEST_TAG)
            .build()

        workManager.enqueue(workRequest)
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
    }

    companion object {
        const val AX_WORK_REQUEST_TAG = "AX_WORK_REQUEST_TAG"
    }
}