package com.example.colorpicker.colorWheel

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.colorpicker.R
import kotlin.math.min

class ColorPickerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs), ColorObservable {

    private var initialColor = Color.BLACK

    private var observers: MutableList<ColorObserver>? = mutableListOf()

    private var colorWheelView: ColorWheelView
    private var observableOnDuty: ColorObservable
    private var onlyUpdateOnTouchEventUp = false

    init {
        orientation = VERTICAL
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ColorPickerView)
        //val enableBrightness = typedArray.getBoolean(R.styleable.ColorPickerView_enableBrightness, true)
        onlyUpdateOnTouchEventUp = typedArray.getBoolean(R.styleable.ColorPickerView_onlyUpdateOnTouchEventUp, false)
        typedArray.recycle()

        colorWheelView = ColorWheelView(context)
        observableOnDuty = colorWheelView
        val density = resources.displayMetrics.density
        val margin = (8 * density).toInt()

        val params = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        addView(colorWheelView, params)
        setPadding(margin, margin, margin, margin)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val maxWidth = MeasureSpec.getSize(widthMeasureSpec)
        val maxHeight = MeasureSpec.getSize(heightMeasureSpec)

        Log.d( "Log", "onMeasure: maxWidth: $maxWidth, maxHeight: $maxHeight")

        val desiredWith = maxHeight - ((paddingTop + paddingBottom) + (paddingLeft + paddingRight))

        Log.d( "Log", "onMeasure: desiredWith: $desiredWith")

        val width = min(maxWidth, desiredWith)
        val height = width - ((paddingLeft + paddingRight) + (paddingTop + paddingBottom))

        Log.d( "Log", "onMeasure: Width: $width, Height: $height")

        super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.getMode(widthMeasureSpec)),
            MeasureSpec.makeMeasureSpec(height, MeasureSpec.getMode(heightMeasureSpec)))
    }

    fun setInitialColor(color: Int) {
        initialColor = color
        colorWheelView.setColor(color, true)
    }

    fun setSelectorColor(color: Int){
        colorWheelView.updateSelector(color)
    }

    override fun subscribe(observer: ColorObserver) {
        observableOnDuty.subscribe(observer)
        observers?.add(observer)
    }

    override fun unsubscribe(observer: ColorObserver) {
        observableOnDuty.unsubscribe(observer)
        observers?.remove(observer)
    }

    override fun getColor(): Int {
        return observableOnDuty.getColor()
    }
}