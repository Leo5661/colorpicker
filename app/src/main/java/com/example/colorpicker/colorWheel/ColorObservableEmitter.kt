package com.example.colorpicker.colorWheel

class ColorObservableEmitter: ColorObservable {

    private val observers: MutableList<ColorObserver> = mutableListOf()
    private var color: Int = -1

    override fun subscribe(observer: ColorObserver) {
        observers.add(observer)
    }

    override fun unsubscribe(observer: ColorObserver) {
        observers.remove(observer)
    }

    override fun getColor(): Int {
        return color
    }

    fun onColor(color: Int, fromUser: Boolean, shouldPropagate: Boolean){
        this.color = color
        for (observer in observers){
            observer.onColor(color, fromUser, shouldPropagate)
        }
    }
}