@file:Suppress("DEPRECATION")

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

    private var mCamera: Camera? = null        // объект Camera
    private lateinit var mPreview: SurfaceView // preview

    private val DELAY_PREVIEW = 500L         // Задержка перезапуска Preview
    private val DELAY_FLASH   = 1000L        // в обычном состоянии и со вспышкой

    var mContext : Context = context          // MainActivity
    private var mFrame : FrameLayout = frame  // frame для отображение вида с камеры

    var mCameraType : CAMERA_TYPE = CAMERA_TYPE.BACK   // Фронтальная или задняя камера
    private var mFlashType : FLASH = FLASH.FLASH_OFF   // Состояние всыпшки

    private var mCameraIdBack  : Int = 0
    private var mCameraIdFront : Int = 0    // Id фронтальной и задней камера

    private var mPictureFile : File? = null   // Файл сохранения снимка
    
    private var isShowLines : Boolean = false
            
    init{
        setCamerasId()              // Получение Id фронтальной и задней камер
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

    // Инициализация камеры
    private fun initCamera(id : Int){
        mCamera = getCameraInstance(id)
        mPreview = CameraPreview(this, mCamera!! )
        mFrame.addView(mPreview)
    }

    // Получние Id камер
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

    // Переключение фронтальной и задней камеры
    fun changeCamera(cameraId : CAMERA_TYPE){
        mCameraType = cameraId
        val newId : Int = when(mCameraType){
            CAMERA_TYPE.BACK -> mCameraIdBack
            CAMERA_TYPE.FRONT -> mCameraIdFront
        }

        // Остановка текущей камеры
        stopCamera()

        // Создание новой камеры
        mCamera = getCameraInstance(newId)
        mPreview = CameraPreview(this, mCamera!!)

        mFrame.removeAllViews()
        mFrame.addView(mPreview)

    }

    // Переключение режима работы вспышки
    fun turnFlash(flash : FLASH){
        if (mCameraType == CAMERA_TYPE.BACK){
            mFlashType = flash
            val parameters = mCamera?.parameters
            when(mFlashType){
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

    // Сделать снимок
    fun takePhoto() {
        try {
            mCamera?.takePicture(null, null, mPicture)
        } catch (e : Exception){
            Toast.makeText(mContext, "Error capture photo", Toast.LENGTH_SHORT).show()
        }
        mPictureFile = null

        // Задержка в зависимости от состояния вспышки
        if (mFlashType == FLASH.FLASH_ON || mFlashType == FLASH.FLASH_AUTO)
            delayPreview(DELAY_PREVIEW + DELAY_FLASH)
        else
            delayPreview(DELAY_PREVIEW)
    }

    // Функция задержки и рестарта Preview
    private fun delayPreview(DELAY : Long){
        Handler().postDelayed({
            restartPreview()
        }, DELAY)
    }

    // Перезапуск preview после снимка
    private fun restartPreview(){
        isShowLines = false
        try {
            mCamera?.startPreview()
        } catch (e : Exception){
            Toast.makeText(mContext, "Error start preview", Toast.LENGTH_SHORT).show()
        }
    }

    // Остановка текущей камеры
    fun stopCamera(){
        mCamera?.stopPreview()
        mCamera?.release()
        mCamera = null
    }

    // Звук снимка
    private fun callShutterSound(){
        val audioManager = mContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val volume = audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM)
        if (volume != 0) {
            val mp = MediaPlayer.create(mContext,
                    Uri.parse("file:///system/media/audio/ui/camera_click.ogg"))
            mp?.start()
        }
    }

    // Добавление снимка в галерею
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

			// Запись данных в файл
            Toast.makeText(mContext, "Success", Toast.LENGTH_SHORT).show()
            val fos = FileOutputStream(mPictureFile)
            // TODO rotation problem
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


    // Создание пустого файла
    private fun getOutputMediaFile(): File? {
        val mediaStorageDir = File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp")
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null
            }
        }
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())

        return File(mediaStorageDir.path + File.separator +
                "IMG_" + timeStamp + ".jpg")
    }

   fun drawLines(angle_1: Double, angle_2 : Double){
       val pref = mContext.getSharedPreferences("MY_SETTINGS", Context.MODE_PRIVATE)
       val line = Line(mContext, angle_1, angle_2,
               pref.getInt("isSwitchHorizLine", 0) == 1,
                pref.getInt("isSwitchVertLine", 0) == 1)
       try {
           if (isShowLines) {
               mFrame.removeViewAt(2)
           }
           isShowLines = true
           mFrame.addView(line, 2)
       }
       catch (e : Exception){}
       finally {}

   }

    //Создание сетки
    fun setGrid(grids : MutableMap<GRID_TYPE, Boolean>, color : String){
        val grid = Grid(mContext, grids, color)
        mFrame.addView(grid, 1)
        restartPreview()
    }
    
}