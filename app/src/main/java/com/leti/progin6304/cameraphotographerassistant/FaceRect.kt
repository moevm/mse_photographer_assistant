package com.leti.progin6304.cameraphotographerassistant

import android.content.Context
import android.graphics.*
import android.hardware.Camera
import android.view.View
import android.view.WindowManager


class FaceRect(context: Context, faces: Array<out Camera.Face>?, val mCameraType: CAMERA_TYPE) : View(context) {

    private lateinit var mPaint: Paint
    private lateinit var mTextPaint: Paint
    private var mDisplayOrientation: Float = 1F
    private var mOrientation: Float = 0F
    private var mFaces: Array<out Camera.Face>? = faces

    private fun initialize() {
        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.isDither = true
        mPaint.color = Color.GREEN
        mPaint.alpha = 128
        mPaint.style = Paint.Style.FILL_AND_STROKE

        mTextPaint = Paint()
        mTextPaint.isAntiAlias = true
        mTextPaint.isDither = true
        mTextPaint.textSize = 20F
        mTextPaint.color = Color.parseColor("#003200")
        mTextPaint.style = Paint.Style.FILL

        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val rot = wm.defaultDisplay.rotation

        when(rot){
            0 -> mDisplayOrientation = 90F
            1 -> mDisplayOrientation = 0F
            3 -> mDisplayOrientation = 180F
        }
    }

    override fun onDraw(canvas: Canvas) {
        initialize()
        canvas.drawLine(0f,0f,0f,0f,mPaint)
        if (mFaces != null) {
            val matrix = Matrix()
            if (mCameraType == CAMERA_TYPE.FRONT)
                prepareMatrix(matrix, true, mDisplayOrientation, width, height)
            else
                prepareMatrix(matrix, false, mDisplayOrientation, width, height)
            canvas.save()
            matrix.postRotate(mOrientation)
            canvas.rotate(-mOrientation)
            val rectF = RectF()
            for ((i, face) in mFaces!!.withIndex()) {
                rectF.set(face.rect)
                matrix.mapRect(rectF)
                canvas.drawRect(rectF, mPaint)
                canvas.drawText("Face $i", rectF.left, rectF.bottom, mTextPaint)
            }
            canvas.restore()
        }
    }

    private fun prepareMatrix(matrix: Matrix, mirror: Boolean, displayOrientation: Float,
                              viewWidth: Int, viewHeight: Int) {

        matrix.setScale(if (mirror) (-1).toFloat() else 1F, 1F)
        matrix.postRotate(displayOrientation)
        matrix.postScale(viewWidth / 2000f, viewHeight / 2000f)
        matrix.postTranslate(viewWidth / 2f, viewHeight / 2f)
    }
}