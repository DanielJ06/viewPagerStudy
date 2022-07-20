package com.tab.storiesclone.ui.main

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TimerViewModel : ViewModel() {
    private lateinit var timerInstance: CountDownTimer
    val lastRegisteredTime: MutableLiveData<Long> = MutableLiveData(0)
    val timerState: MutableLiveData<TimesStates> = MutableLiveData()

    fun startTimer(storiesTimerDelay: Long = 8000) {
        if (timerState.value !== TimesStates.RUNNING) {
            timerState.value = TimesStates.RUNNING
            timerInstance = object : CountDownTimer(storiesTimerDelay, 1000) {
                override fun onTick(p0: Long) {
                    lastRegisteredTime.postValue(p0)
                }

                override fun onFinish() {
                    timerState.postValue(TimesStates.FINISHED)
                }
            }.start()
        }
    }

    fun resumeTimer(totalTime: Long) {
        if (timerState.value == TimesStates.STOPPED) {
            timerState.value = TimesStates.RUNNING
            lastRegisteredTime.value?.let {
                startTimer(totalTime - it)
            }
        }
    }

    fun stopTimer() {
        if (timerState.value == TimesStates.RUNNING) {
            timerState.value = TimesStates.STOPPED
            timerInstance.cancel()
        }
    }

}

enum class TimesStates {
    RUNNING,
    STOPPED,
    FINISHED
}