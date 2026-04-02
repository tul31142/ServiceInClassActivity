package edu.temple.myapplication

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.os.Looper
import android.widget.Button
import java.util.Timer
import java.util.logging.Handler
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {

    //Interact with the Timer
    //

    lateinit var timerBinder: TimerService.TimerBinder
    var isConnected = false


    val serviceConnection = object : ServiceConnection{
        override fun onServiceConnected(
            name: ComponentName?,
            service: IBinder?
        ) {
            timerBinder =  service as TimerService.TimerBinder
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
            if (isConnected){
                timerBinder.start(30)


            }

        }
        //bind and unbind a service
        findViewById<Button>(R.id.stopButton).setOnClickListener {
            if(isConnected){
                timerBinder.pause()
            }

        }
    }
}