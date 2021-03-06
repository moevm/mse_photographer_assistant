package com.leti.progin6304.photographerassistant

import android.view.SurfaceHolder
import android.view.SurfaceView
import android.content.Context
import android.hardware.Camera
import android.util.Log
import android.view.Surface
import android.view.WindowManager
import android.widget.Toast

import java.io.IOException

class CameraPreview(
        private var mCameraActivity: CameraActivity,
        private var mCamera: Camera,
        private var flash: FLASH? = null
) : SurfaceView(mCameraActivity.mContext), SurfaceHolder.Callback {


    private val mHolder: SurfaceHolder = holder.apply {
        addCallback(this@CameraPreview)
        setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
    }

    private fun startFaceDetection() {
        try {if (mCamera.parameters.maxNumDetectedFaces > 0) mCamera.stopFaceDetection(); mCamera.startFaceDetection()} catch (e : Exception){}
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        mCamera.apply {
            try {
                setParameters()
                setPreviewDisplay(holder)
                setGrids()
                startPreview()
            } catch (e: IOException) {
                Log.d("TAGME", "Error setting camera preview: ${e.message}")
            }
        }
        startFaceDetection()
    }

    // Получение информации со страницы настроек и инициализация сеток
    private fun setGrids(){
        val pref = mCameraActivity.mContext.getSharedPreferences("MY_SETTINGS", Context.MODE_PRIVATE)

        // Создание словаря из типа сетки в отображение сетки
        val grids : MutableMap<GRID_TYPE, Boolean> = mutableMapOf()
        grids[GRID_TYPE.GRID3X3] = pref.getInt("isSwitchGridRectangle3x3", 0) == 1
        grids[GRID_TYPE.GRIDFIB] = pref.getInt("isSwitchGridFib", 0) == 1
        grids[GRID_TYPE.GRIDSQUARE] = pref.getInt("isSwitchGridSquare", 0) == 1
        grids[GRID_TYPE.GRIDCENTER] = pref.getInt("isSwitchGridCenter", 0) == 1

        mCameraActivity.setGrid(grids, pref.getString("colorGrid", "#FFFFFF"))

    }

    // Установка параметров
    private fun setParameters(){
        val parameters = mCamera.parameters
        parameters?.jpegQuality = 80
        if (mCameraActivity.mCameraType == CAMERA_TYPE.BACK)
            parameters?.focusMode = Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE

        val pref = mCameraActivity.mContext.getSharedPreferences("MY_SETTINGS", Context.MODE_PRIVATE)
        parameters.colorEffect = pref.getString("filter", Camera.Parameters.EFFECT_NONE)

        //Поворот View
        val display = (mCameraActivity.mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        if (display.rotation == Surface.ROTATION_0) {
            mCamera.setDisplayOrientation(90)
        } else if (display.rotation == Surface.ROTATION_90) {
            mCamera.setDisplayOrientation(0)
        } else if (display.rotation == Surface.ROTATION_270) {
            mCamera.setDisplayOrientation(180)
        }
		
		//Вычисление наибольшего разрешения
        val sizes = parameters!!.supportedPictureSizes
        var size: Camera.Size = sizes!![0]
        for (i in sizes.indices) {
            if (sizes[i].width > size.width) {
                size = sizes[i]
            }
        }

        // Установка найденных размеров
        parameters.setPictureSize(size.width, size.height)

        if (flash != null) {
            when(flash){
                FLASH.FLASH_ON -> parameters.flashMode = Camera.Parameters.FLASH_MODE_ON
                FLASH.FLASH_OFF -> parameters.flashMode = Camera.Parameters.FLASH_MODE_OFF
                FLASH.FLASH_AUTO -> parameters.flashMode = Camera.Parameters.FLASH_MODE_AUTO
            }
        }

        try {
            mCamera.parameters = parameters          //Установка новых параметров
        } catch (e :Exception){
            Toast.makeText(mCameraActivity.mContext, "Can't set parametrs", Toast.LENGTH_SHORT).show()
        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        mCamera.release()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, w: Int, h: Int) {
        if (mHolder.surface == null) {
            return
        }
        try {
            mCamera.stopPreview()
        } catch (e: Exception) {
        }

        mCamera.apply {
            try {
                setParameters()
                setPreviewDisplay(mHolder)
                startPreview()
            } catch (e: Exception) {
                Log.d("TAGME", "Error starting camera preview: ${e.message}")
            }
        }
        startFaceDetection()
    }
}

