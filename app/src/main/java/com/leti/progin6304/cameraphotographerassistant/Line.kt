package com.leti.progin6304.cameraphotographerassistant

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.view.View
import android.view.WindowManager

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
        if (isShowHorizLine){
            val dh = Math.tan(angleHorizLine) * w / 2
            canvas.drawLine(0f, (h / 2 - dh).toFloat(), w.toFloat(), (h /2 + dh).toFloat(), paint)
        }
        if (isShowVertLine){
            val dh = Math.tan(angleVertLines) * w / 2
            canvas.drawLine(0f, (h / 2 - dh).toFloat(), w.toFloat(), (h /2 + dh).toFloat(), paint)
        }
        
    }
}