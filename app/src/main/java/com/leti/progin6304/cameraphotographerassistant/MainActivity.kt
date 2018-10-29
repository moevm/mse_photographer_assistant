package com.leti.progin6304.cameraphotographerassistant


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var mCameraActivity : CameraActivity? = null     // Камера
    private var mFlash : FLASH = FLASH.FLASH_OFF             // Текущее состояние вспышки
    private var mCameraType : CAMERA_TYPE = CAMERA_TYPE.BACK // Текущая камера

    private val GENERIC_PERM_HANDLER = 100
    private var actionOnPermission: ((granted: Boolean) -> Unit)? = null
    private var isAskingPermissions = false

    private lateinit var pref : SharedPreferences           // Настройки сеток

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Запрос разрешения на камеру
        handlePermission(Manifest.permission.CAMERA) {
            if (it) {
                // Запрос разрешения на запись в память
                handlePermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) {
                    hideBars()
                    initButtons()
                    initCamera()
                    setPref()
                }
            }
        }

        // TODO rotation problem
    }

    // Получение информации со страницы настроек и инициализация сеток
    private fun setPref(){
        pref = getSharedPreferences("MY_SETTINGS", Context.MODE_PRIVATE)

        // Debug status switches
        //Toast.makeText(this, "3x3: "  + pref.getInt("isSwitchGridRectangle3x3", 0).toString(), Toast.LENGTH_SHORT).show()
        //Toast.makeText(this, "Fib: "  + pref.getInt("isSwitchGridFib", 0).toString(), Toast.LENGTH_SHORT).show()
        //Toast.makeText(this, "Horiz: " + pref.getInt("isSwitchHorizLine", 0).toString(), Toast.LENGTH_SHORT).show()
        //Toast.makeText(this, "Vert: "  + pref.getInt("isSwitchVertLine", 0).toString(), Toast.LENGTH_SHORT).show()

        // TODO implement grid
        if (pref.getInt("isSwitchGridRectangle3x3", 0) == 1){
            mCameraActivity?.setGrid()
            return
        }
        if (pref.getInt("isSwitchGridFib", 0) == 1){
            //showGridFib()
            return
        }

        if (pref.getInt("isSwitchHorizLine", 0) == 1){
            //showHorizLine()
        }
        if (pref.getInt("isSwitchVertLine", 0) == 1){
            //showVertLine()
        }
    }

    //Обработка разрешений
    private fun hasPermission(permId: String) = ContextCompat.checkSelfPermission(this, permId) == PackageManager.PERMISSION_GRANTED;

    private fun handlePermission(permissionId: String, callback: (granted: Boolean) -> Unit) {
        actionOnPermission = null
        if (hasPermission(permissionId)) {
            callback(true)
        } else {
            isAskingPermissions = true
            actionOnPermission = callback
            ActivityCompat.requestPermissions(this, arrayOf(permissionId), GENERIC_PERM_HANDLER)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        isAskingPermissions = false
        if (requestCode == GENERIC_PERM_HANDLER && grantResults.isNotEmpty()) {
            actionOnPermission?.invoke(grantResults[0] == 0)
        }
    }

    //  Иницализация камеры, создание объекта класса CameraActivity
    private fun initCamera(){
        mCameraActivity = CameraActivity(this, camera_preview)
    }


    // Скрытие верхней панели
    private fun hideBars(){
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide()
    }

    // Обработчики нажатий на кнопки
    private fun initButtons(){
        settings.setOnClickListener { launchSettings() }
        shutter.setOnClickListener{ launchShutter() }
        switchCamera.setOnClickListener{ changeCamera()}
        flash.setOnClickListener{ turnFlash()}
    }

    // Смена камеры
    private fun changeCamera(){
        mCameraType = when(mCameraType){
            CAMERA_TYPE.FRONT -> CAMERA_TYPE.BACK
            CAMERA_TYPE.BACK -> CAMERA_TYPE.FRONT
        }
        mCameraActivity?.changeCamera(mCameraType)
    }


    // Переключение режима вспышки
    private fun turnFlash(){
        mFlash = when (mFlash){
            FLASH.FLASH_OFF -> FLASH.FLASH_ON
            FLASH.FLASH_ON -> FLASH.FLASH_AUTO
            FLASH.FLASH_AUTO -> FLASH.FLASH_OFF
        }
        // Смена иконки
        changeFlashIcon(mFlash)

        // Смена параметров вспышки камеры
        mCameraActivity?.turnFlash(mFlash)
    }


    // Изменение иконки
    private fun changeFlashIcon(flash_: FLASH){
        when (flash_){
            FLASH.FLASH_OFF  -> flash.setImageResource(R.mipmap.ic_flash_off)
            FLASH.FLASH_ON   -> flash.setImageResource(R.mipmap.ic_flash_on)
            FLASH.FLASH_AUTO -> flash.setImageResource(R.mipmap.ic_flash_auto)
        }
    }

    // Переход в настройки
    private fun launchSettings(){
        val intent = Intent(applicationContext, SettingsActivity::class.java)
        startActivity(intent)
        mCameraActivity?.stopCamera()
    }

    // Нажатие кнопки завтора камеры
    private fun launchShutter(){
        mCameraActivity?.takePhoto()
    }
}
