package com.aurosaswat.camerax

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.aurosaswat.camerax.databinding.ActivityDisplayCroppedPhotoBinding
import com.bumptech.glide.Glide
import java.io.File


class DisplayCroppedPhoto : AppCompatActivity() {

    private lateinit var viewBinding:ActivityDisplayCroppedPhotoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityDisplayCroppedPhotoBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        if (intent.hasExtra("BitmapImage")){
            val imageUri =intent.getStringExtra("BitmapImage")
            Glide.with(this)
                .load(Uri.fromFile(File(imageUri)))
                .into(viewBinding.imageView)
        }




    }
}