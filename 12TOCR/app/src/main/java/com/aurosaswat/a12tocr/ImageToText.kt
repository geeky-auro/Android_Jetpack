package com.aurosaswat.a12tocr


import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.aurosaswat.a12tocr.databinding.ActivityImageToTextBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.shashank.sony.fancytoastlib.FancyToast
import java.io.InputStream

@RequiresApi(Build.VERSION_CODES.M)
class ImageToText : AppCompatActivity() {
   private lateinit var viewBinding:ActivityImageToTextBinding
    private val recognizer= TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding=ActivityImageToTextBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        val contentResolver: ContentResolver = this.contentResolver
        val inputStream: InputStream? =contentResolver.openInputStream(Uri.parse(intent.getStringExtra("URI_DATA")))
        val bitmap = BitmapFactory.decodeStream(inputStream)
        viewBinding.imageView.setImageBitmap(bitmap)
        processImage(bitmap)
    }

    private fun processImage(imageBitmap:Bitmap?) {
        if (imageBitmap!=null){
            val image= imageBitmap.let {
                InputImage.fromBitmap(it,0)
            }
            image.let { it ->
                recognizer.process(it)
                    .addOnSuccessListener {
                        showDialog(it.text.toString())
                    }
                    .addOnFailureListener {
                        FancyToast.makeText(this,"Nothing to Show ",FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show()
                    }
            }
        }
        else{
            FancyToast.makeText(this,"Please Select Image First",FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show()

        }

    }

    private fun showDialog(gettext:String){
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottomsheetayout, null)
        val btnClose = view.findViewById<Button>(R.id.idBtnDismiss)
        val text=view.findViewById<TextView>(R.id.idTVCourseName)
        val copyBtn=view.findViewById<ImageButton>(R.id.copy_btn)
        copyBtn.setOnClickListener {
            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("text", gettext)
            clipboardManager.setPrimaryClip(clipData)
            FancyToast.makeText(this,"Text Copied",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show()
        }
        btnClose.setOnClickListener {
            dialog.dismiss()
        }
        text.text=gettext
        dialog.setCancelable(false)
        dialog.setContentView(view)
        dialog.show()
    }


}