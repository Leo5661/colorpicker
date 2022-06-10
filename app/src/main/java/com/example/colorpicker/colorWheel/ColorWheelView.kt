package com.example.colorpicker.colorWheel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.colorpicker.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.*

class ColorWheelView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs), ColorObservable, Updatable {

    private var radius = 0f
    private var centerX = 0f
    private var centerY = 0f
    private var netWidth: Int = 0
    private var netHeight: Int = 0

    private var selectorRadiusPx = (Constants.SELECTOR_RADIUS_DP * 3).toFloat()

    private var currentPoint = PointF()
    private var currentColor = Color.WHITE
    private var onlyUpdateOnTouchEventUp: Boolean = false

    private lateinit var selector: ColorWheelSelector
    private var emitter = ColorObservableEmitter()
    private var handler = TouchEventHandler(this)

    init {
        selectorRadiusPx = Constants.SELECTOR_RADIUS_DP * resources.displayMetrics.density

        run {
            val layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            val palette = ColorWheelPalette(context)
            val padding = selectorRadiusPx.toInt()
            palette.setPadding(padding, padding, padding, padding)
            addView(palette, layoutParams)
        }

        run {
            val layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            selector = ColorWheelSelector(context)
            selector.setSelectorRadiusPx(selectorRadiusPx)
            addView(selector, layoutParams)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val maxWidth = MeasureSpec.getSize(widthMeasureSpec)
        val maxHeight = MeasureSpec.getSize(heightMeasureSpec)

        val width: Int
        var height: Int
        width = min(maxWidth, maxHeight).also { height = it }
        super.onMeasure(
            MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        )
    }

    fun setOnlyUpdateOnTouchEventUp(onlyUpdateOnTouchEventUp: Boolean) {
        this.onlyUpdateOnTouchEventUp = onlyUpdateOnTouchEventUp
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event!!.actionMasked) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_MOVE -> {
                handler.onTouchEvent(event)
                return true
            }
            MotionEvent.ACTION_UP -> {
                update(event)
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    fun updateSelector(color: Int){
        Log.d("Log", "updateSelector: The color is :  $color")
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        netWidth = w - paddingLeft - paddingRight
        netHeight = h - paddingTop - paddingBottom
        radius = min(netWidth, netHeight) * 0.5f - selectorRadiusPx
        if (radius < 0) return
        centerX = netWidth * 0.5f
        centerY = netHeight * 0.5f
        setColor(currentColor, false)
    }

    fun setColor(color: Int, shouldPropagate: Boolean) {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        val r = hsv[1] * radius
        val radian = (hsv[0] / 180f * Math.PI).toFloat()
        updateSelector(
            (r * cos(radian.toDouble()) + centerX).toFloat(),
            (-r * sin(radian.toDouble()) + centerY).toFloat()
        )
        currentColor = color
        if (!onlyUpdateOnTouchEventUp) {
            emitter.onColor(color, false, shouldPropagate)
        }
    }

//    fun setColorOnClick(color: Int){
//        val bitmap = Bitmap.createBitmap(this.netWidth, this.netHeight, Bitmap.Config.ARGB_8888)
//        val height = bitmap.height
//        val width = bitmap.width
//
//        Log.d("Log", "get View Dim: $width , $height ")
//
//        CoroutineScope(Dispatchers.IO).launch {
//            for (y in 0 until height) {
//                for (x in 0 until width) {
//                    val pixel = bitmap.getPixel(x, y)
//
//                    val r = Color.red(pixel)
//                    val g = Color.green(pixel)
//                    val b = Color.blue(pixel)
//
//                    val pixelColor = Color.rgb(r, g, b)
//                    Log.d("Log", "color: $color , $pixelColor ")
//                    if (pixelColor == abs(color)) {
//                        withContext(Dispatchers.Main){
//                            currentPoint.x = x.toFloat()
//                            currentPoint.y = y.toFloat()
//                            selector.setCurrentPoint(currentPoint)
//                        }
//                    }
//                }
//            }
//        }
//    }

    override fun subscribe(observer: ColorObserver) {
        emitter.subscribe(observer)
    }

    override fun unsubscribe(observer: ColorObserver) {
        emitter.unsubscribe(observer)
    }

    override fun getColor(): Int {
        return emitter.getColor()
    }

    override fun update(event: MotionEvent) {
        val x = event.x
        val y = event.y
        val isTouchUpEvent = event.actionMasked == MotionEvent.ACTION_UP
        if (!onlyUpdateOnTouchEventUp || isTouchUpEvent) {
            emitter.onColor(getColorAtPoint(x, y), true, isTouchUpEvent)
        }
        updateSelector(x, y)
    }

    private fun getColorAtPoint(eventX: Float, eventY: Float): Int {
        val x = eventX - centerX
        val y = eventY - centerY
        val r = sqrt((x * x + y * y).toDouble())
        val hsv = floatArrayOf(0f, 0f, 1f)
        hsv[0] = (atan2(y.toDouble(), -x.toDouble()) / Math.PI * 180f).toFloat() + 180
        hsv[1] = max(0f, min(1f, (r / radius).toFloat()))
        return Color.HSVToColor(hsv)
    }

    private fun updateSelector(eventX: Float, eventY: Float) {
        var x = eventX - centerX
        var y = eventY - centerY
        val r = sqrt((x * x + y * y).toDouble())
        if (r > radius) {
            x *= (radius / r).toFloat()
            y *= (radius / r).toFloat()
        }
        currentPoint.x = x + centerX
        currentPoint.y = y + centerY
        selector.setCurrentPoint(currentPoint)
    }

}