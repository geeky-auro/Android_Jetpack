package com.aurosaswat.camerax

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.aurosaswat.camerax.databinding.ActivityDisplayCroppedPhotoBinding


class DisplayCroppedPhoto : AppCompatActivity() {

    private lateinit var viewBinding:ActivityDisplayCroppedPhotoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityDisplayCroppedPhotoBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        if (intent.hasExtra("BitmapImage")){
            var bitmap=BitmapFactory.decodeByteArray(
                intent.getByteArrayExtra("byteArray"),0, intent.getByteArrayExtra("byteArray")!!.size)
            viewBinding.imageView.setImageBitmap(bitmap)
        }




    }
}