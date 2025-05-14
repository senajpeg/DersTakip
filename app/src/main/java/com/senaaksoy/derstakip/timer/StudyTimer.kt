package com.senaaksoy.derstakip.timer

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class StudyTimer {
    private val _elapsedTime = MutableStateFlow(0L)//bura geçen süreyi milisaniye cinsinden tutacak
    val elapsedTime: StateFlow<Long> = _elapsedTime.asStateFlow()

    private var timerJob: Job? = null//zamanlayıcının çalışan coroutine işini temsil ediyo
    private var lastTimestamp = 0L// zamanlayıcının son çalıştırılma zamanı
    private var isRunning = false

    private val coroutineScope = CoroutineScope(Dispatchers.Default)//bu timerı arka planda çalıştırıyor

    fun start() {
        if (isRunning) return

        isRunning = true
        lastTimestamp = System.currentTimeMillis() //mevcut zamanı yakalıyor

        timerJob = coroutineScope.launch {
            while (isRunning) {
                delay(100)
                val currentTimestamp = System.currentTimeMillis()
                val delta = currentTimestamp - lastTimestamp
                _elapsedTime.value += delta
                lastTimestamp = currentTimestamp
            }
        }
    }

    fun pause() {
        isRunning = false
        timerJob?.cancel()//coroutine işini iptal ediyor burada
    }

    fun reset() {
        pause()
        _elapsedTime.value = 0L
    }

    fun resume() {
        start()
    }
    fun setElapsedTime(timeInMillis: Long) { //zamanlayıcıyı belli bir değere ayarlar
        pause()
        _elapsedTime.value = timeInMillis
    }

    fun formatTime(millis: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(millis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

}