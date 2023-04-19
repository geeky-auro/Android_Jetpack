package com.aurosaswat.a12tocr

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.aurosaswat.a12tocr.databinding.ActivityHomeScreenBinding


class HomeScreenActivity : AppCompatActivity() {
    private lateinit var viewBinding:ActivityHomeScreenBinding
    var SELECT_IMAGE=200
    @RequiresApi(Build.VERSION_CODES.M)
    private val contract=registerForActivityResult(ActivityResultContracts.GetContent()){
        val intent = Intent(this@HomeScreenActivity, ImageToText::class.java)
        intent.putExtra("URI_DATA",it.toString())
        startActivity(intent)
    }
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding=ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.CameraPic.setOnClickListener{
            val intent=Intent(this@HomeScreenActivity,CameraXActivity::class.java)
            startActivity(intent)
        }
        viewBinding.selectImage.setOnClickListener {
            imageChooser()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun imageChooser() {
//        val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//        intent.type = "image/*"
//        startActivityForResult(intent, PICK_IMAGE_REQUEST)

        // pass the constant to compare it
        // with the returned requestCode

        // pass the constant to compare it
        // with the returned requestCode
//        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_IMAGE)

        contract.launch("image/*")


    }


}