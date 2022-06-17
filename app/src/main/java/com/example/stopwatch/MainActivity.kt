package com.example.stopwatch

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map

class MainActivity : AppCompatActivity() {

    private val timestampProvider = object : TimestampProvider {
        override fun getMilliseconds(): Long {
            return System.currentTimeMillis()
        }
    }
    private val stopwatchListOrchestrator = StopwatchListOrchestrator(
        StopwatchStateHolder(
            StopwatchStateCalculator(timestampProvider, ElapsedTimeCalculator(timestampProvider)),
            ElapsedTimeCalculator(timestampProvider),
            TimestampMillisecondsFormatter()
        ),
        CoroutineScope(Dispatchers.Main + SupervisorJob())
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.text_time)
        CoroutineScope(Dispatchers.Main + SupervisorJob()).launch {
            stopwatchListOrchestrator.ticker.collect() {
                    textView.text = it
            }
        }
        val textView2 = findViewById<TextView>(R.id.text_time2)
        CoroutineScope(Dispatchers.Main + SupervisorJob()).launch {
            delay(1000)
            stopwatchListOrchestrator.ticker2.collect(){
                textView2.text = it
            }
        }

        findViewById<Button>(R.id.button_start2).setOnClickListener {
            stopwatchListOrchestrator.start()
            Toast.makeText(this, "start 2", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.button_start).setOnClickListener {
            stopwatchListOrchestrator.start()
            Toast.makeText(this, "start", Toast.LENGTH_SHORT).show()
        }
        findViewById<Button>(R.id.button_pause).setOnClickListener {
            stopwatchListOrchestrator.pause()
            Toast.makeText(this, "pause", Toast.LENGTH_SHORT).show()
        }
        findViewById<Button>(R.id.button_stop).setOnClickListener {
            stopwatchListOrchestrator.stop()
            Toast.makeText(this, "stop", Toast.LENGTH_SHORT).show()
        }

    }
}

