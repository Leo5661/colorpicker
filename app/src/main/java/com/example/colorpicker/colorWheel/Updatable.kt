package com.example.colorpicker.colorWheel

import android.view.MotionEvent

interface Updatable {
    fun update(event: MotionEvent)
}