package com.leti.progin6304.cameraphotographerassistant

import android.content.Context
import kotlinx.android.synthetic.main.activity_main.*

import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        hideBars()
        initButtons()
    }

    private fun hideBars(){
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide()
    }

    private fun initButtons(){
        settings.setOnClickListener { launchSettings() }
        shutter.setOnClickListener{ launchShutter() }
        grid.setOnClickListener{ launchGrid() }
    }

    private fun launchSettings(){
        val intent = Intent(applicationContext, SettingsActivity::class.java)
        startActivity(intent)
    }

    private fun launchShutter(){
        Toast.makeText(this, "There will be a photo launch",
                Toast.LENGTH_LONG).show()
        callShutterSound()
    }

    private fun launchGrid(){
        Toast.makeText(this, "There will be a grid selection",
                Toast.LENGTH_LONG).show()
    }

    private fun callShutterSound(){
        val audioManager = this.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val volume = audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM)
        if (volume != 0) {
            val mp = MediaPlayer.create(this, Uri.parse("file:///system/media/audio/ui/camera_click.ogg"))
            mp?.start()
        }
    }
}
