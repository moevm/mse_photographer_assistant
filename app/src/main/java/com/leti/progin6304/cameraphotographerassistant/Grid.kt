package com.leti.progin6304.cameraphotographerassistant

import android.content.Context
import android.graphics.*
import android.view.View
import android.view.WindowManager
import java.util.*
import kotlin.math.sqrt

fun getRandomColor(): Int {
    val rnd = Random()
    return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
}

class Grid(context: Context, private val grids : MutableMap<GRID_TYPE, Boolean>, val color : Int) : View(context) {
    override fun onDraw(canvas: Canvas) {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width = size.x
        val height = size.y

        val paint = Paint()

        var colorString = "#FFFFFF"
        when (color){
            0 -> colorString = "#FFFFFF"
            1 -> colorString = "#A9A9A9"
            2 -> colorString = "#000000"
            3 -> colorString = String.format("#%06x", getRandomColor())
        }
        paint.color = Color.parseColor(colorString)
        paint.strokeWidth = 10F
        paint.style = Paint.Style.STROKE
        paint.isAntiAlias = true
        paint.isDither = true
        //paint.setARGB(255, 255, 0, 0)

        for ((grid, isDraw) in grids){
            if (grid == GRID_TYPE.GRID3X3 && isDraw){
                val x1 = width / 3
                val y1 = height / 3

                canvas.drawLine(x1.toFloat(), 0f, x1.toFloat(), height.toFloat(), paint)
                canvas.drawLine(x1.toFloat() * 2, 0f, x1.toFloat() * 2, height.toFloat(), paint)

                canvas.drawLine(0f, y1.toFloat(), width.toFloat(), y1.toFloat(), paint)
                canvas.drawLine(0f, y1.toFloat() * 2, width.toFloat(), y1.toFloat() * 2, paint)
            }

            if (grid == GRID_TYPE.GRIDFIB && isDraw){

                var path : Path = Path()
                path.moveTo(0F, 0F)

                val oval = RectF()
                var w : Float = width.toFloat()
                var h : Float = height.toFloat()

                oval.set(0f, -h * 8 / 34, w*2, h)
                canvas.drawArc(oval, 90f, 90f, true, paint)

                oval.set(0F, 0f, w * 13 / 21 * 2, h * 13 / 34 * 2)
                canvas.drawArc(oval, 180f, 90f, true, paint)

                oval.set(w * 5 / 21, 0f, w, h * 16 / 34)
                canvas.drawArc(oval, 270f, 90f, true, paint)

                oval.set(w * 11 / 21, h * 3 / 34, w, h * 13 / 34)
                canvas.drawArc(oval, 0f, 90f, true, paint)

                oval.set(w * 13 / 21, h * 7 / 34, w * 19 / 21, h * 13 / 34)
                canvas.drawArc(oval, 90f, 90f, true, paint)

                oval.set(w * 13 / 21, h * 8 / 34, w * 17 / 21, h * 12 / 34)
                canvas.drawArc(oval, 180f, 90f, true, paint)

                oval.set(w * 14 / 21, h * 8 / 34, w * 16 / 21, h * 10 / 34)
                canvas.drawArc(oval, 270f, 180f, true, paint)
            }

            if (grid == GRID_TYPE.GRIDSQUARE && isDraw){
                var w = width  / sqrt(5F)
                var h = height / sqrt(5F)

                val s = w * h
                w = sqrt(s)
                h = w

                if (h >= height || w >= width){
                    w = width  / sqrt(5F)
                    h = height / sqrt(5F)
                }

                val startX = (width - w) / 2
                val startY = (height - h) / 2

                canvas.drawLine(startX, startY, startX, startY + h, paint)
                canvas.drawLine(startX, startY, startX + w, startY, paint)

                canvas.drawLine(startX, startY + h, startX + w, startY + h, paint)
                canvas.drawLine(startX + w, startY, startX + w, startY + h, paint)

            }
        }


    }
}