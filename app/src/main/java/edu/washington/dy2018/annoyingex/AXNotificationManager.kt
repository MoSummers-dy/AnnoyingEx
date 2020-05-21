package edu.washington.dy2018.annoyingex

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.*
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class AXNotificationManager(private val context: Context) {
    private val notificationManagerCompat = NotificationManagerCompat.from(context)
    private var workManager = WorkManager.getInstance(context)
    private var messageApiManager: MessageApiManager
    var listOfMessages: List<String>

    init {
        createChannel()
        messageApiManager = (context as AnnoyingExApp).messageApiManager
        listOfMessages = messageApiManager.listOfMessages
        messageApiManager.getListOfMessages {
            listOfMessages = messageApiManager.listOfMessages
        }
        startFetching()
    }

    fun postItNote() {
        val randomIndex = Random.nextInt(listOfMessages.size)

        val mainIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(MESSAGE, listOfMessages[randomIndex])
        }

        val pendingMainIntent = PendingIntent.getActivity(context, Random.nextInt(), mainIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_sentiment_very_dissatisfied_black_24dp)
            .setContentTitle("You Know Who Again")
            .setContentText(listOfMessages[randomIndex])
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingMainIntent)
            .setAutoCancel(true)
            .build()

        notificationManagerCompat.notify(randomIndex, notification)
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notifications"
            val descriptionText = "Messages from the annoying ex"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            notificationManagerCompat.createNotificationChannel(channel)
        }
    }

    // Extra Credit 2
    private fun startFetching() {
        if (isFetchRunning()) {
            stopWork()
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

    private fun stopWork() {
        workManager.cancelAllWorkByTag(FETCH_WORK_REQUEST_TAG)
    }

    companion object {
        const val CHANNEL_ID = "CHANNELID"
        const val MESSAGE = "MESSAGE"
        const val FETCH_WORK_REQUEST_TAG = "AX_WORK_REQUEST_TAG"
    }

}