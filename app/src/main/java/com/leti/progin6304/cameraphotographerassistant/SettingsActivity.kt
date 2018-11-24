package com.leti.progin6304.cameraphotographerassistant

import android.app.AlertDialog
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import kotlinx.android.synthetic.main.activity_settings.*
import java.util.*


fun Boolean.toInt() = if (this) 1 else 0
fun Int.toBoolean() = this != 0

fun getRandomColor(): String {
    val rnd = Random()
    return String.format("#%06x", Color.argb(255, rnd.nextInt(256),
            rnd.nextInt(256), rnd.nextInt(256)))
}


class SettingsActivity : AppCompatActivity() {

    private lateinit var pref : SharedPreferences
    private lateinit var builder : AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        title = "Settings"

        setSwitch()
        initSwitch()
        initColor()
    }

    private fun initColor(){
        builder = AlertDialog.Builder(this)
        builder.setTitle("Выберите цвет линий сетки")

        val colors = arrayOf("Белый", "Серый", "Черный", "Произвольный")
        val editor = pref.edit()
        builder.setItems(colors) { _, which ->
            when (which) {
                0 -> {editor.putString("colorGrid", "#FFFFFF"); editor.apply();}
                1 -> {editor.putString("colorGrid", "#A9A9A9"); editor.apply();}
                2 -> {editor.putString("colorGrid", "#000000"); editor.apply();}
                3 -> {editor.putString("colorGrid", getRandomColor()); editor.apply();}
            }
        }

        changeColor.setOnClickListener{
            val dialog = builder.create()
            dialog.show()
        }

    }

    // Установка положений переключателей на старте
    private fun setSwitch(){
        pref = getSharedPreferences("MY_SETTINGS", Context.MODE_PRIVATE)
        switchGridRectangle3x3.isChecked = pref.getInt("isSwitchGridRectangle3x3", 0).toBoolean()
        switchGridFib.isChecked = pref.getInt("isSwitchGridFib", 0).toBoolean()
        switchGridSquare.isChecked = pref.getInt("isSwitchGridSquare", 0).toBoolean()
        switchGridCenter.isChecked = pref.getInt("isSwitchGridCenter", 0).toBoolean()
        switchHorizLine.isChecked = pref.getInt("isSwitchHorizLine", 0).toBoolean()
        switchVertLine.isChecked = pref.getInt("isSwitchVertLine", 0).toBoolean()

    }

    // Инициализация обработки нажатия
    private fun initSwitch(){
        switchGridRectangle3x3.setOnClickListener{
            launchSwitchGrid3x3()
        }
        switchGridFib.setOnClickListener {
            launchSwitchGridFib()
        }
        switchGridSquare.setOnClickListener{
            launchSwitchGridSquare()
        }
        switchGridCenter.setOnClickListener{
            launchSwitchGridCenter()
        }
        switchHorizLine.setOnClickListener{
            launchSwitchHorizLine()
        }
        switchVertLine.setOnClickListener{
            launchSwitchVertLine()
        }
    }

    // Обновление информации отображения сетки 3х3
    private fun launchSwitchGrid3x3(){
        if (switchGridRectangle3x3.isChecked){
            switchHorizLine.isChecked = false
            switchVertLine.isChecked = false
        }
        updatePref()
    }

    // Обновление информации отображения сетки Фибоначчи
    private fun launchSwitchGridFib(){
        if (switchGridFib.isChecked){
            switchHorizLine.isChecked = false
            switchVertLine.isChecked = false
        }
        updatePref()
    }

    // Обновление информации отображения сетки центра экрана
    private fun launchSwitchGridCenter(){
        if (switchGridCenter.isChecked){
            switchHorizLine.isChecked = false
            switchVertLine.isChecked = false
        }
        updatePref()
    }

    // Обновление информации отображения сетки 1/5 площади
    private fun launchSwitchGridSquare(){
        if (switchGridSquare.isChecked){
            switchHorizLine.isChecked = false
            switchVertLine.isChecked = false
        }
        updatePref()
    }

    // Обновление информации отображение линии горизонта
    private fun launchSwitchHorizLine(){
        if (switchHorizLine.isChecked){
            switchGridRectangle3x3.isChecked = false
            switchGridFib.isChecked = false
            switchGridCenter.isChecked = false
            switchGridSquare.isChecked = false
        }
        updatePref()
    }

    // Обновление информации отображение линии вертикали
    private fun launchSwitchVertLine(){
        if (switchVertLine.isChecked){
            switchGridRectangle3x3.isChecked = false
            switchGridFib.isChecked = false
            switchGridCenter.isChecked = false
            switchGridSquare.isChecked = false
        }
        updatePref()
    }


    // Обновление информации всех сеток
    private fun updatePref(){
        val editor = pref.edit()
        editor.putInt("isSwitchGridRectangle3x3", switchGridRectangle3x3.isChecked.toInt())
        editor.putInt("isSwitchGridFib",  switchGridFib.isChecked.toInt())
        editor.putInt("isSwitchGridSquare", switchGridSquare.isChecked.toInt())
        editor.putInt("isSwitchGridCenter", switchGridCenter.isChecked.toInt())
        editor.putInt("isSwitchHorizLine", switchHorizLine.isChecked.toInt())
        editor.putInt("isSwitchVertLine", switchVertLine.isChecked.toInt())
        editor.apply()
    }

    // Возврат по кнопке домой
    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
