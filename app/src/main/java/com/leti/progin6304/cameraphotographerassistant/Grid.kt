package com.leti.progin6304.cameraphotographerassistant

import android.content.Context
import android.graphics.*
import android.view.View
import android.view.WindowManager
import kotlin.math.PI
import android.R.attr.path



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

        if (type == GRID_TYPE.GRIDFIB){

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

    }
}