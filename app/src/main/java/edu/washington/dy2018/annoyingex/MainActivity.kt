package edu.washington.dy2018.annoyingex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.washington.dy2018.annoyingex.AXNotificationManager.Companion.MESSAGE
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val annoyingExApp = (application as AnnoyingExApp)

        btnGoAgain.setOnClickListener {
            annoyingExApp.annoyExManager.startAnnoyingEx()
        }

        btnBlock.setOnClickListener {
            annoyingExApp.annoyExManager.stopWork()
        }

        // Extra Credit 1
        val dataFromNotification = intent.getStringExtra(MESSAGE)
        tvMessage.text = dataFromNotification;
    }
}
