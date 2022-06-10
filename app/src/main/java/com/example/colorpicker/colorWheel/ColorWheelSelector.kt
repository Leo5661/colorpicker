package com.example.colorpicker.colorWheel

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View
import com.example.colorpicker.Constants

class ColorWheelSelector @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var selectorPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var selectorRadiusPx: Float = (Constants.SELECTOR_RADIUS_DP * 5).toFloat()
    private var currentPoint = PointF()
    private var color: Int = 0


    init {
        selectorPaint.color = Color.WHITE
        selectorPaint.style = Paint.Style.STROKE
        selectorPaint.setShadowLayer((selectorRadiusPx * .5F), 0F, 0F, Color.BLACK )
        selectorPaint.strokeWidth = 5F
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawCircle(currentPoint.x, currentPoint.y, selectorRadiusPx * 1.5f, selectorPaint)
    }

    fun setSelectorRadiusPx(selectorRadiusPx: Float) {
        this.selectorRadiusPx = selectorRadiusPx
    }

    fun setCurrentPoint(currentPoint: PointF) {
        this.currentPoint = currentPoint
        invalidate()
    }

    fun setFillColor(){
    }
}