package com.example.colorpicker.colorWheel

interface ColorObservable {

    fun subscribe(observer: ColorObserver)

    fun unsubscribe(observer: ColorObserver)

    fun getColor(): Int

}