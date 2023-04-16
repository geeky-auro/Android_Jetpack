package com.aurosaswat.camerax

import android.R.attr
import android.content.ContentResolver
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.aurosaswat.camerax.databinding.ActivityCameraDisplayBinding
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


@RequiresApi(Build.VERSION_CODES.M)
class CameraDisplayActivity : AppCompatActivity() {



    private lateinit var viewBinding: ActivityCameraDisplayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityCameraDisplayBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val contentResolver: ContentResolver = CameraDisplayActivity@ this.getContentResolver()
        val inputStream: InputStream? =contentResolver.openInputStream(Uri.parse(intent.getStringExtra("URI_DATA")))
        val bitmap = BitmapFactory.decodeStream(inputStream)
        viewBinding.cropImageView.setImageUriAsync(Uri.parse(intent.getStringExtra("URI_DATA")))
        viewBinding.cropImageView.setImageBitmap(bitmap)
        var uriI:Uri?
        viewBinding.cropImageView.setOnCropImageCompleteListener{view,result->
            val croppedImageUri=result.uriContent
            uriI=croppedImageUri
        }

        viewBinding.cropImageView.croppedImageAsync()


        viewBinding.button.setOnClickListener{

//            val bs=ByteArrayOutputStream()
//            cropped?.compress(Bitmap.CompressFormat.PNG,50,bs)
            val cropped = viewBinding.cropImageView.getCroppedImage()

//            getDir() method is used to get or create a directory inside the app's internal storage space.
            var cw=ContextWrapper(applicationContext)
            var directory=cw.getDir("CroppedImg", Context.MODE_PRIVATE)
            if(!directory.exists()){
                directory.mkdir()
            }

            var myPath= File(directory,"cropped.png")
            var fos:FileOutputStream?=null
            try{
                fos=FileOutputStream(myPath)
                Toast.makeText(CameraDisplayActivity@this,"$myPath",Toast.LENGTH_SHORT).show()
                cropped?.compress(Bitmap.CompressFormat.PNG,100,fos)
                fos.close()

            }catch (e:Exception){
                Log.d("SAVE_IMAGE",e.message,e)
            }
            finally {
                val intent = Intent(this, DisplayCroppedPhoto::class.java)
                intent.putExtra("BitmapImage", myPath.absolutePath)
                startActivity(intent)
            }

        }


    }


}