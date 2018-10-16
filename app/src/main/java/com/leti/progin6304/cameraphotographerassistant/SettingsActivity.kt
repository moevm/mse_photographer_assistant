package com.leti.progin6304.cameraphotographerassistant

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent



class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        title = "Settings"

    }

    // Возврат по кнопке домой
    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
