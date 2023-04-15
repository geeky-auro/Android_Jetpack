package com.aurosaswat.camerax

import android.R.attr
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.aurosaswat.camerax.databinding.ActivityCameraDisplayBinding
import java.io.ByteArrayOutputStream
import java.io.InputStream


@RequiresApi(Build.VERSION_CODES.M)
class CameraDisplayActivity : AppCompatActivity() {



    private lateinit var viewBinding: ActivityCameraDisplayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityCameraDisplayBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        val contentResolver: ContentResolver = CameraDisplayActivity@ this.getContentResolver()
        val inputStream: InputStream? =contentResolver.openInputStream(Uri.parse(intent.getStringExtra("URI_DATA")))
        val bitmap = BitmapFactory.decodeStream(inputStream)
        viewBinding.cropImageView.setImageUriAsync(Uri.parse(intent.getStringExtra("URI_DATA")))

        viewBinding.button.setOnClickListener{
            val cropped = viewBinding.cropImageView.getCroppedImage()
            val bs=ByteArrayOutputStream()
            cropped?.compress(Bitmap.CompressFormat.PNG,50,bs)
            val intent = Intent(this, DisplayCroppedPhoto::class.java)
            intent.putExtra("BitmapImage", bs.toByteArray())
            startActivity(intent)
        }


    }


}