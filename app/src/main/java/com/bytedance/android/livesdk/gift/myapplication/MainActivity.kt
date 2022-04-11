package com.bytedance.android.livesdk.gift.myapplication

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import io.reactivex.Scheduler
import io.reactivex.internal.operators.observable.ObservableTimer
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val mainScope = MainScope()
    private val defaultScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val defaultScope2 = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val ioScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val globalScope = GlobalScope

    @SuppressLint("LongLogTag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val job = mainScope.launch {
            try {
                delay(100000)
            }catch (e: Exception) {
                Log.d("YANYAO mainScope - finally", "cancelled")
            }
            Log.d("YANYAO mainScope - launch", Thread.currentThread().name)

            coroutineScope{
                Log.d("YANYAO mainScope - coroutineScope", Thread.currentThread().name)
            }

            withContext(mainScope.coroutineContext){
                Log.d("YANYAO mainScope - withContext", Thread.currentThread().name)
            }

            Log.d("YANYAO mainScope - afterCoroutineScope", Thread.currentThread().name)

        }

        defaultScope.launch {
            delay(500)
            Log.d("YANYAO defaultScope - launch", Thread.currentThread().name)
        }

        defaultScope2.launch {
            Log.d("YANYAO defaultScope2 - launch", Thread.currentThread().name)
        }

        ioScope.launch {
            Log.d("YANYAO ioScope - launch", Thread.currentThread().name)
        }

        globalScope.launch {
            Log.d("YANYAO globalScope - start", Thread.currentThread().name)

            launch(context = mainScope.coroutineContext){
                Log.d("YANYAO globalScope - launch - mainScope", Thread.currentThread().name)

            }

            delay(1000)
            job.cancel()

            runBlocking {
                Log.d("YANYAO globalScope - runBlocking", Thread.currentThread().name)
            }

            Log.d("YANYAO globalScope - after runBlocking", Thread.currentThread().name)
        }

        Log.d("YANYAO Function - Before Running Block", Thread.currentThread().name)

        runBlocking {
            Log.d("YANYAO Function - Running Block", Thread.currentThread().name)
        }

        Log.d("YANYAO Function - After Running Block", Thread.currentThread().name)

        Log.d("YANYAO Function - Last line", Thread.currentThread().name)
    }
}
