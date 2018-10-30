package com.leti.progin6304.cameraphotographerassistant

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.view.View
import android.view.WindowManager

class Grid(context: Context, val type : GRID_TYPE) : View(context) {
    override fun onDraw(canvas: Canvas) {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width = size.x
        val height = size.y

        val paint = Paint()
        paint.color = Color.parseColor("#FFFFFF")
        paint.strokeWidth = 10F
        paint.style = Paint.Style.STROKE
        paint.isAntiAlias = true
        paint.isDither = true
        //paint.setARGB(255, 255, 0, 0)

        if (type == GRID_TYPE.GRID3X3){
            val x1 = width / 3
            val y1 = height / 3

            canvas.drawLine(x1.toFloat(), 0f, x1.toFloat(), height.toFloat(), paint)
            canvas.drawLine(x1.toFloat() * 2, 0f, x1.toFloat() * 2, height.toFloat(), paint)

            canvas.drawLine(0f, y1.toFloat(), width.toFloat(), y1.toFloat(), paint)
            canvas.drawLine(0f, y1.toFloat() * 2, width.toFloat(), y1.toFloat() * 2, paint)
        }

    }
}