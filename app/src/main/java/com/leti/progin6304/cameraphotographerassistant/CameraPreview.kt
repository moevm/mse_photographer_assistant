package com.leti.progin6304.cameraphotographerassistant

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
        private var mContext: Context,
        private var mCamera: Camera,
        private var mCameraId: CAMERA_TYPE
) : SurfaceView(mContext), SurfaceHolder.Callback {


    private val mHolder: SurfaceHolder = holder.apply {
        addCallback(this@CameraPreview)
        setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
    }


    override fun surfaceCreated(holder: SurfaceHolder) {
        mCamera.apply {
            try {
                setParameters()
                setPreviewDisplay(holder)
                startPreview()
            } catch (e: IOException) {
                Log.d("TAGME", "Error setting camera preview: ${e.message}")
            }
        }
    }

    // Установка параметров
    private fun setParameters(){
        val parameters = mCamera.parameters
        parameters?.jpegQuality = 80
        if (mCameraId == CAMERA_TYPE.BACK)
            parameters?.focusMode = Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE

        val display = (mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        if (display.rotation == Surface.ROTATION_0) {
            mCamera.setDisplayOrientation(90)
        } else if (display.rotation == Surface.ROTATION_270) {
            mCamera.setDisplayOrientation(180)
        }

        val sizes = parameters!!.supportedPictureSizes
        var size: Camera.Size = sizes!![0]
        for (i in sizes.indices) {
            if (sizes[i].width > size.width) {
                size = sizes[i]
            }
        }

        parameters.setPictureSize(size.width, size.height)
        try {
            mCamera.parameters = parameters
        } catch (e :Exception){
            Toast.makeText(context, "Can't set parametrs", Toast.LENGTH_SHORT).show()
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


        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        mCamera.apply {
            try {
                setParameters()
                setPreviewDisplay(mHolder)
                startPreview()
            } catch (e: Exception) {
                Log.d("TAGME", "Error starting camera preview: ${e.message}")
            }
        }
    }
}

