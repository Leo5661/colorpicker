package com.example.colorpicker.colorWheel

interface ColorObserver {
    fun onColor(color: Int, fromUser: Boolean, shouldPropagate: Boolean)
}