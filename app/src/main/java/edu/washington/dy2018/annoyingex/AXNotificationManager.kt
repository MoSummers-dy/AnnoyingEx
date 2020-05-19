package edu.washington.dy2018.annoyingex

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlin.random.Random

class AXNotificationManager(private val context: Context) {
    private val notificationManagerCompat = NotificationManagerCompat.from(context)
    private var messageApiManager: MessageApiManager
    private var listOfMessages: List<String>
    private var counter = 0

    init {
        createChannel()
        messageApiManager = (context as AnnoyingExApp).messageApiManager
        listOfMessages = messageApiManager.listOfMessages
        messageApiManager.getListOfMessages({messageList ->
            listOfMessages = messageApiManager.listOfMessages
        }, {})
    }

    fun postItNote() {
        val mainIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingMainIntent = PendingIntent.getActivity(context, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_sentiment_very_dissatisfied_black_24dp)
            .setContentTitle("You Know Who Again")
            .setContentText(listOfMessages[Random.nextInt(listOfMessages.size)])
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingMainIntent)
            .setAutoCancel(true)
            .build()

        counter++
        notificationManagerCompat.notify(counter, notification)
    }

    fun createChannel() {
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

    companion object {
        const val CHANNEL_ID = "CHANNELID"
    }

}