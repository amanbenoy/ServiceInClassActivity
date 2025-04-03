package edu.temple.myapplication

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import java.util.Timer

class MainActivity : AppCompatActivity() {

    private lateinit var timerBinder: TimerService.TimerBinder
    var isConnected = false

    val timerHandler = Handler(Looper.getMainLooper()){
        findViewById<TextView>(R.id.textView).text = it.what.toString()
        true
    }

    val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            timerBinder = service as TimerService.TimerBinder
            timerBinder.setHandler(timerHandler)
            isConnected = true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isConnected = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindService(
            Intent(this, TimerService::class.java),
            serviceConnection,
            BIND_AUTO_CREATE
        )

        findViewById<Button>(R.id.startButton).setOnClickListener {
                if (isConnected) {
                    if (!timerBinder.isRunning) {
                        timerBinder.start(100)
                    }
                    else if (!timerBinder.paused)
                        timerBinder.pause()
                }
            }


        findViewById<Button>(R.id.stopButton).setOnClickListener {
            timerBinder.stop()
        }
    }
}