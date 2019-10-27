package com.leti.progin6304.photographerassistant

import android.content.Context
import android.hardware.Camera
import android.widget.FrameLayout

class MyFaceDetectionListener(private val mContext : Context, private val mFrame : FrameLayout,
                              private val mCameraType : CAMERA_TYPE) : Camera.FaceDetectionListener {
    override fun onFaceDetection(faces: Array<out Camera.Face>?, p1: Camera?) {
        val pref = mContext.getSharedPreferences("MY_SETTINGS", Context.MODE_PRIVATE)
        if (pref.getInt("isSwitchFace", 0) == 1)
            if (faces != null){
                try{mFrame.removeViewAt(3)} catch (e : Exception){}
                val mFaceView = FaceRect(mContext, faces, mCameraType)
                mFrame.addView(mFaceView, 3)
            }
    }
}