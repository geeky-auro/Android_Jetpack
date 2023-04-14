package com.aurosaswat.camerax

import androidx.appcompat.app.AppCompatActivity
import android.annotation.SuppressLint
import android.net.Uri
import android.os.*
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowInsets
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.aurosaswat.camerax.databinding.ActivityImagePreviewBinding
import com.bumptech.glide.Glide

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class ImagePreviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityImagePreviewBinding
    private lateinit var fullscreenContent: ImageView
    private lateinit var fullscreenContentControls: LinearLayout
    private val hideHandler = Handler(Looper.myLooper()!!)

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(binding.root)
        val imageUri =intent.getStringExtra("IMAGE_URI")
//        val glide
        Log.d("ImagePreview","Uri is $imageUri")
//        val glide=Glide.with(this)
//        glide.load(imageUri)
//            .into(binding.fullscreenContent)

    }
}