package com.aurosaswat.a12tocr


import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentResolver
import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.aurosaswat.a12tocr.databinding.ActivityImageToTextBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
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
        //                    Display the text
                        viewBinding.viewText.text=it.text
        //                        showDialog(it.text)
                    }
                    .addOnFailureListener {
                        Toast.makeText(this,"Nothing to Show ", Toast.LENGTH_SHORT).show()
                    }
            }
        }
        else{
            Toast.makeText(this,"Please Select Image First", Toast.LENGTH_SHORT).show()
        }

    }

    private fun showDialog(text:String){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottomsheetayout)
        val tView=findViewById<TextView>(R.id.chooseText)
        val copy=findViewById<ImageButton>(R.id.copy)
        tView.text=text
        copy.setOnClickListener {
            val clipBoardManager:ClipboardManager= getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip:ClipData=ClipData.newPlainText("copiedText",text)
            clipBoardManager.setPrimaryClip(clip)
            clip.description
            Toast.makeText(this@ImageToText,"Text Copied Successfully",Toast.LENGTH_SHORT).show()
        }
        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)

    }


}