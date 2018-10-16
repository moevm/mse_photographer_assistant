package com.leti.progin6304.cameraphotographerassistant


import android.content.ContentValues
import android.content.Context
import android.hardware.Camera
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.provider.MediaStore.Images
import android.util.Log
import android.view.SurfaceView
import android.widget.FrameLayout
import android.widget.Toast
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class CameraActivity(context: Context, frame : FrameLayout) {

    private var mCamera: Camera? = null       // объект Camera
    private lateinit var mPreview: SurfaceView//

    private var mContext : Context = context  // MainActivity
    private var mFrame : FrameLayout = frame

    private var mCameraType : CAMERA_TYPE = CAMERA_TYPE.BACK
    private var mCameraIdBack  : Int = 0
    private var mCameraIdFront : Int = 0

    private var mPictureFile : File? = null   // Файл сохранения снимка


    init{
        setCamerasId()               // Получение Id фронтальной и задней камер
        initCamera(mCameraIdBack)   // Инициализация задней камеры
    }

    // Запуск камеры
    private fun getCameraInstance(id : Int): Camera? {
        return try {
            Camera.open(id)
        } catch (e: Exception) {
            null
        }
    }

    private fun initCamera(id : Int){
        mCamera = getCameraInstance(id)
        mPreview = CameraPreview(mContext, mCamera!!, mCameraType!!)
        mFrame.addView(mPreview)
    }

    private fun setCamerasId(){
        val numberOfCameras = Camera.getNumberOfCameras()
        for (i in 0 until numberOfCameras) {
            val info : Camera.CameraInfo = Camera.CameraInfo()
            Camera.getCameraInfo(i, info)
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT)
                mCameraIdFront = i
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK)
                mCameraIdBack = i
        }
    }

    fun changeCamera(cameraId : CAMERA_TYPE){
        mCameraType = cameraId
        val newId : Int = when(mCameraType){
            CAMERA_TYPE.BACK -> mCameraIdBack
            CAMERA_TYPE.FRONT -> mCameraIdFront
        }

        stopCamera()

        mCamera = getCameraInstance(newId)

        mPreview = CameraPreview(mContext, mCamera!!, mCameraType!!)

        mFrame.removeAllViews()
        mFrame.addView(mPreview)

    }

    fun turnFlash(flash : FLASH){
        if (mCameraType == CAMERA_TYPE.BACK){
            val parameters = mCamera?.parameters
            when(flash){
                FLASH.FLASH_ON   ->  parameters?.flashMode = Camera.Parameters.FLASH_MODE_ON
                FLASH.FLASH_OFF  ->  parameters?.flashMode = Camera.Parameters.FLASH_MODE_OFF
                FLASH.FLASH_AUTO ->  parameters?.flashMode = Camera.Parameters.FLASH_MODE_AUTO
            }
            mCamera?.parameters = parameters

            try {
                mCamera?.startPreview()
            } catch (e : Exception){
                Toast.makeText(mContext, "Error start after changing flash", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun takePhoto() {
        try {
            mCamera?.takePicture(null, null, mPicture)
        } catch (e : Exception){
            Toast.makeText(mContext, "Error capture photo", Toast.LENGTH_SHORT).show()
        }
        mPictureFile = null

        Handler().postDelayed({
            restartPreview()
        }, 500)
    }


    private fun restartPreview(){
        try {
            mCamera?.startPreview()
        } catch (e : Exception){
            Toast.makeText(mContext, "Error start preview", Toast.LENGTH_SHORT).show()
        }
    }

    fun stopCamera(){
        mCamera?.stopPreview()
        mCamera?.release()
        mCamera = null
    }

    private fun callShutterSound(){
        val audioManager = mContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val volume = audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM)
        if (volume != 0) {
            val mp = MediaPlayer.create(mContext, Uri.parse("file:///system/media/audio/ui/camera_click.ogg"))
            mp?.start()
        }
    }

    private fun addImageToGallery(filePath: String, context: Context) {
        val values = ContentValues()
        values.put(Images.Media.DATE_TAKEN, System.currentTimeMillis())
        values.put(Images.Media.MIME_TYPE, "image/jpeg")
        values.put(MediaStore.MediaColumns.DATA, filePath)
        context.contentResolver.insert(Images.Media.EXTERNAL_CONTENT_URI, values)
    }



    private val mPicture = Camera.PictureCallback { data, _ ->
        mPictureFile = getOutputMediaFile() ?: run {
            return@PictureCallback
        }
        try {

            Toast.makeText(mContext, "Success", Toast.LENGTH_SHORT).show()
            val fos = FileOutputStream(mPictureFile)
            //val display = (mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
            //var angle = 0.0F
            //val matrix = Matrix()
            //matrix.postRotate(angle)
            //val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
            //Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true).compress(Bitmap.CompressFormat.JPEG, 100, fos)

            fos.write(data)
            fos.close()


            addImageToGallery(mPictureFile!!.path, mContext)
            callShutterSound()

        } catch (e: FileNotFoundException) {
            Log.d("TAG", "File not found: ${e.message}")
        } catch (e: IOException) {
            Log.d("TAG", "Error accessing file: ${e.message}")
        }
    }


    private fun getOutputMediaFile(): File? {
        val mediaStorageDir = File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp")
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                //Log.d("MyCameraApp", "failed to create directory")
                return null
            }
        }
        // Create a media file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())

        return File(mediaStorageDir.path + File.separator +
                "IMG_" + timeStamp + ".jpg")
    }
}