package com.aurosaswat.a12tocr

import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.camera.core.Camera
import androidx.cardview.widget.CardView
import com.aurosaswat.a12tocr.databinding.ActivityHomeScreenBinding

class HomeScreenActivity : AppCompatActivity() {
    private lateinit var viewBinding:ActivityHomeScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding=ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.CameraPic.setOnClickListener{
            val intent=Intent(this@HomeScreenActivity,CameraXActivity::class.java)
            startActivity(intent)
        }

        viewBinding.selectImage.setOnClickListener {
            Toast.makeText(this@HomeScreenActivity,"Under Construction",Toast.LENGTH_SHORT).show()
        }


    }
}