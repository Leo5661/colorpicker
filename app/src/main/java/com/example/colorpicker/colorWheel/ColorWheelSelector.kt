package com.example.colorpicker.colorWheel

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.example.colorpicker.Constants

class ColorWheelSelector @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr)   {

    private var selectorPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var selectorRadiusPx: Float = (Constants.SELECTOR_RADIUS_DP * 5).toFloat()
    private var currentPoint = PointF()
    private var color: Int? = Color.WHITE

    init {
        selectorPaint.color = color!!
        selectorPaint.style = Paint.Style.FILL

        paint.color = Color.WHITE
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 10F
        selectorPaint.setShadowLayer((selectorRadiusPx * .5F), 0F, 0F, Color.BLACK)

    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawCircle(currentPoint.x, currentPoint.y, selectorRadiusPx * 1.35f, selectorPaint)
        canvas.drawCircle(currentPoint.x, currentPoint.y, selectorRadiusPx* 1.5f, paint)
    }

    fun setSelectorRadiusPx(selectorRadiusPx: Float) {
        this.selectorRadiusPx = selectorRadiusPx
    }

    fun setCurrentPoint(currentPoint: PointF, color: Int?) {
        this.currentPoint = currentPoint
        Log.d("Log", "getColor: $color")
        if (color != null) selectorPaint.setColor(color)
        invalidate()
    }


}