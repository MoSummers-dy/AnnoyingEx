package edu.washington.dy2018.annoyingex

import android.app.Application

class AnnoyingExApp:Application() {
    lateinit var messageApiManager: MessageApiManager
        private set

    lateinit var axNotificationManager: AXNotificationManager
        private set

    lateinit var annoyExManager: AnnoyExManager
        private set

    override fun onCreate(){
        super.onCreate()
        // load manager
        messageApiManager = MessageApiManager(this)
        axNotificationManager = AXNotificationManager(this)
        annoyExManager = AnnoyExManager(this)
    }
}