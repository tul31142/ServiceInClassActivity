package edu.temple.myapplication

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import java.util.Timer
import java.util.logging.Handler
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {

    //Interact with the Timer
    //

    lateinit var timerBinder: TimerService.TimerBinder
    var isConnected = false
    lateinit var textView: TextView

    val serviceConnection = object : ServiceConnection{
        override fun onServiceConnected(
            name: ComponentName?,
            service: IBinder?
        ) {
            timerBinder =  service as TimerService.TimerBinder
            timerBinder.setHandler(timerHandler)
            isConnected = true

        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isConnected = false
        }

    }

    val timerHandler = android.os.Handler(Looper.getMainLooper()){
        textView.text = it.what.toString()
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById<TextView>(R.id.textView)


        bindService(
            Intent(this, TimerService::class.java),
            serviceConnection,
            BIND_AUTO_CREATE
        )

        findViewById<Button>(R.id.startButton).setOnClickListener {
            if (isConnected && !timerBinder.isRunning){
                timerBinder.start(30)
                findViewById<Button>(R.id.startButton).text = "Paused"


            }
            else if (isConnected && timerBinder.isRunning){
                timerBinder.pause()
                findViewById<Button>(R.id.startButton).text = "Un-Paused"

            }



        }
        //bind and unbind a service
        findViewById<Button>(R.id.stopButton).setOnClickListener {
            if(isConnected){
                timerBinder.stop()
                textView.text = "0"
                findViewById<Button>(R.id.startButton).text = "Start"


            }

        }
    }
}