package com.leti.progin6304.cameraphotographerassistant

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.view.View
import android.view.WindowManager
import kotlin.math.abs

class Line(context: Context, private var angleHorizLine : Double,
                             private var angleVertLines : Double,
                             private val isShowHorizLine : Boolean,
                             private val isShowVertLine : Boolean ) : View(context) {

    override fun onDraw(canvas: Canvas) {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val size = Point()
        display.getSize(size)

        val paint = Paint()

        paint.color = Color.parseColor("#FFFFFF")
        paint.strokeWidth = 8F
        paint.style = Paint.Style.STROKE
        paint.isAntiAlias = true
        paint.isDither = true

        val w : Double = size.x.toDouble()
        val h : Double = size.y.toDouble()

        if (w >= h) {
            val tmp = angleHorizLine
            angleHorizLine = angleVertLines
            angleVertLines = tmp
        }

        val pi = Math.PI
        if (isShowHorizLine){
            if (abs(angleHorizLine) <= pi / 18 ||
                    (abs(angleHorizLine) <= pi * 19 / 18 && abs(angleHorizLine) >= pi * 17 / 18))
                paint.color = Color.GREEN
            else
                paint.color = Color.WHITE
            val dh = Math.tan(angleHorizLine) * w / 2
            canvas.drawLine(0f, (h / 2 - dh).toFloat(), w.toFloat(), (h /2 + dh).toFloat(), paint)
        }
        if (isShowVertLine){
            if ((abs(angleVertLines) >= pi * 4 / 9 && abs(angleVertLines) <= pi * 5 / 9) ||
                    (abs(angleVertLines) <= pi * 14 / 9  && abs(angleVertLines) >= pi * 13 / 9))
                paint.color = Color.GREEN
            else
                paint.color = Color.WHITE
            val dh = Math.tan(angleVertLines) * w / 2
            canvas.drawLine(0f, (h / 2 - dh).toFloat(), w.toFloat(), (h /2 + dh).toFloat(), paint)
        }
        
    }
}