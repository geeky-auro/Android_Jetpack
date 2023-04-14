package com.aurosaswat.camerax

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.aurosaswat.camerax.databinding.ActivityCameraDisplayBinding
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageView
import java.io.InputStream

@RequiresApi(Build.VERSION_CODES.M)
class CameraDisplayActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityCameraDisplayBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding=ActivityCameraDisplayBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)



        val contentResolver: ContentResolver = CameraDisplayActivity@this.getContentResolver()
        val inputStream: InputStream? = contentResolver.openInputStream(Uri.parse(intent.getStringExtra("URI_DATA")))
        val bitmap = BitmapFactory.decodeStream(inputStream)
        viewBinding.imageView.setImageBitmap(bitmap)


    }

    


}