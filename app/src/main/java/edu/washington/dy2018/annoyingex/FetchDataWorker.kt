package edu.washington.dy2018.annoyingex

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class FetchDataWorker(private val context: Context, workerParameters: WorkerParameters) : Worker(context, workerParameters) {
    override fun doWork(): Result {
        val axApp = context as AnnoyingExApp
        axApp.messageApiManager.getListOfMessages{
            axApp.axNotificationManager.listOfMessages = it
        }
        return Result.success()
    }
}