package edu.washington.dy2018.annoyingex

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class PostNotificationWorker(private val context: Context, workerParameters: WorkerParameters) : Worker(context, workerParameters) {
    override fun doWork(): Result {
        Log.i("dy", "start worker")
        val axApp = context as AnnoyingExApp
        axApp.axNotificationManager.postItNote()
        return Result.success()
    }
}