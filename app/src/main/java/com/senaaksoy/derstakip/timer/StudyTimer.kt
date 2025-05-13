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
    private val _elapsedTime = MutableStateFlow(0L)
    val elapsedTime: StateFlow<Long> = _elapsedTime.asStateFlow()

    private var timerJob: Job? = null
    private var lastTimestamp = 0L
    private var isRunning = false

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    fun start() {
        if (isRunning) return

        isRunning = true
        lastTimestamp = System.currentTimeMillis()

        timerJob = coroutineScope.launch {
            while (isRunning) {
                delay(100) // Update every 100ms for smooth UI
                val currentTimestamp = System.currentTimeMillis()
                val delta = currentTimestamp - lastTimestamp
                _elapsedTime.value += delta
                lastTimestamp = currentTimestamp
            }
        }
    }

    fun pause() {
        isRunning = false
        timerJob?.cancel()
    }

    fun reset() {
        pause()
        _elapsedTime.value = 0L
    }

    fun resume() {
        start()
    }

    fun formatTime(millis: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(millis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}