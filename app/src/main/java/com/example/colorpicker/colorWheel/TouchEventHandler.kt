package com.example.colorpicker.colorWheel

import android.view.MotionEvent
import com.example.colorpicker.Constants

class TouchEventHandler(var updatable: Updatable) {

    private var minInterval: Int = Constants.EVENT_MIN_INTERVAL
    private var lastPassedEventTime: Long = 0

    fun onTouchEvent(event: MotionEvent?) {
        val current = System.currentTimeMillis()
        if (current - lastPassedEventTime <= minInterval) {
            return
        }
        lastPassedEventTime = current
        updatable.update(event!!)
    }
}