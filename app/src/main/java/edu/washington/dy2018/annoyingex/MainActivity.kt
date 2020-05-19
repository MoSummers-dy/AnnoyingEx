package edu.washington.dy2018.annoyingex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val annoyingExApp = (application as AnnoyingExApp)

        btnGoAgain.setOnClickListener {
            annoyingExApp.annoyExManager.startAnnoyingEx()
        }

        btnBlock.setOnClickListener() {
            annoyingExApp.annoyExManager.stopWork()
        }
    }
}
