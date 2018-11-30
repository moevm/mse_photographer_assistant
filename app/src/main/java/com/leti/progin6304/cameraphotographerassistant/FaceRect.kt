package com.leti.progin6304.cameraphotographerassistant

import android.content.Context
import android.graphics.*
import android.hardware.Camera
import android.view.View
import android.view.WindowManager

// Клас, описывающий отрисовку лиц
class FaceRect(context: Context, faces: Array<out Camera.Face>?,
               private val mCameraType: CAMERA_TYPE) : View(context) {

    private lateinit var mPaint: Paint            // Paint для прямоугольника
    private lateinit var mTextPaint: Paint        // Paint для текста
    private var mDisplayOrientation: Float = 1F   // Текуюшая ориентация экрана
    private var mOrientation: Float = 0F          //
    private var mFaces: Array<out Camera.Face>? = faces  // Массив лиц

    private fun initialize() {
        // Описание параметров прямоугольника
        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.isDither = true
        mPaint.color = Color.GREEN
        mPaint.alpha = 128
        mPaint.style = Paint.Style.FILL_AND_STROKE

        // Описание параметров текста
        mTextPaint = Paint()
        mTextPaint.isAntiAlias = true
        mTextPaint.isDither = true
        mTextPaint.textSize = 20F
        mTextPaint.color = Color.parseColor("#003200")
        mTextPaint.style = Paint.Style.FILL

        // Установка параметров поворта прямоугльников в зависимости от ориентации устройства
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val rot = wm.defaultDisplay.rotation

        when(rot){
            0 -> mDisplayOrientation = 90F
            1 -> mDisplayOrientation = 0F
            3 -> mDisplayOrientation = 180F
        }
    }

    // Переопределенный метод Draw
    override fun onDraw(canvas: Canvas) {
        initialize()
        if (mFaces != null) {

            // Поворот canvas
            val matrix = Matrix()
            prepareMatrix(matrix, mCameraType == CAMERA_TYPE.FRONT,
                    mDisplayOrientation, width, height)
            canvas.save()
            matrix.postRotate(mOrientation)
            canvas.rotate(-mOrientation)
            val rectF = RectF()

            // Отрисовка прямоугольников
            for ((i, face) in mFaces!!.withIndex()) {
                rectF.set(face.rect)
                matrix.mapRect(rectF)
                canvas.drawRect(rectF, mPaint)
                canvas.drawText("Face ${i+1}", rectF.left, rectF.bottom, mTextPaint)
            }
            canvas.restore()
        }
    }

    // Функция поворотаж
    private fun prepareMatrix(matrix: Matrix, mirror: Boolean,
                              displayOrientation: Float, viewWidth: Int, viewHeight: Int) {

        matrix.setScale(if (mirror) (-1).toFloat() else 1F, 1F)
        matrix.postRotate(displayOrientation)
        matrix.postScale(viewWidth / 2000f, viewHeight / 2000f)
        matrix.postTranslate(viewWidth / 2f, viewHeight / 2f)
    }
}