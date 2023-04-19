package com.aurosaswat.a12tocr

import android.Manifest
import android.R
import android.content.ContentValues
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.ScaleGestureDetector
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.aurosaswat.a12tocr.databinding.ActivityCameraXactivityBinding
import dev.shreyaspatil.MaterialDialog.MaterialDialog
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


typealias LumaListener = (luma: Double) -> Unit

class CameraXActivity : AppCompatActivity() {

    private lateinit var viewBinding:ActivityCameraXactivityBinding
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService

    @RequiresApi(Build.VERSION_CODES.M)
    private val contract=registerForActivityResult(ActivityResultContracts.GetContent()){
        val intent = Intent(this@CameraXActivity, ImageToText::class.java)
        intent.putExtra("URI_DATA",it.toString())
        startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding=ActivityCameraXactivityBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // Check for Permisssions, if everything is granted then allow the camera to click the picture

        if (allPermissionsGranted()){
            startCamera()
        }else{
            ActivityCompat.requestPermissions(
                this@CameraXActivity,REQUIRED_PERMISIONS,REQUEST_CODE_PERMISSIONS
            )
        }

        viewBinding.imageCaptureButton.setOnClickListener {
            takePhoto()
        }

        viewBinding.SelectImage.setOnClickListener {
            contract.launch("image/*")
        }

        cameraExecutor=Executors.newSingleThreadExecutor()

    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewBinding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            // Select back camera as a default
            val imageAnalzer = ImageAnalysis.Builder().build()
                .also {
                    it.setAnalyzer(cameraExecutor, LuminosityAnalyzer {
                        Log.d(TAG, "Average luminosity: $it")
                    })
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()
                // Bind use cases to camera
                val camera = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture, imageAnalzer
                )

                // Getting the CameraInfo instance from the camera
                val cameraInfo = camera.cameraInfo
                val cameraControl = camera.cameraControl

                /*
                * Implementing Zooming Functionality
                * */
                val listener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
                    override fun onScale(detector: ScaleGestureDetector): Boolean {
                        // Get the camera's current zoom ratio
                        val currentZoomRatio = cameraInfo.zoomState.value?.zoomRatio ?: 0F

                        // Get the pinch gesture's scaling factor
                        val delta = detector.scaleFactor

                        // Update the camera's zoom ratio. This is an asynchronous operation that returns
                        // a ListenableFuture, allowing you to listen to when the operation completes.
                        cameraControl.setZoomRatio(currentZoomRatio * delta)

                        return true
                    }
                }

                val scaleGestureDetector = ScaleGestureDetector(this, listener)
//                Attach the pinch gesture listener to the viewFinder
                viewBinding.viewFinder.setOnTouchListener { v, event ->
                    scaleGestureDetector.onTouchEvent(event)
                    return@setOnTouchListener true
                }
            } catch (exc: Exception) {
                Log.e("CameraXActivity", "Use Case binding Failed", exc)
            }
        }, ContextCompat.getMainExecutor(this))
    }


    private fun allPermissionsGranted()= REQUIRED_PERMISIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }



    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return
        // Create time stamped name and MediaStore entry.
        val name = SimpleDateFormat(FILEMAN_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
            }
        }
//        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        // Save the captured image to the output stream
//        val outputStream = contentResolver.openOutputStream(uri!!)


        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues)
            .build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                @RequiresApi(Build.VERSION_CODES.M)
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults){
                    val msg = "Photo capture succeeded: ${outputFileResults.savedUri}"
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()

//                    val tsavedUri = outputFileResults.savedUri ?: Uri.fromFile(File(outputFileResults.savedFile!!.absolutePath))


                    val intent = Intent(this@CameraXActivity, ImageToText::class.java)
                    intent.putExtra("URI_DATA",outputFileResults.savedUri.toString())
                    startActivity(intent)


                    Log.d(TAG, msg)
                }
            }
        )
    }


    private class LuminosityAnalyzer(private val listener:LumaListener):ImageAnalysis.Analyzer {

        private fun ByteBuffer.toByteArray(): ByteArray {
            rewind()    // Rewind the buffer to zero
            val data = ByteArray(remaining())
            get(data)   // Copy the buffer into a byte array
            return data // Return the byte array
        }

        override fun analyze(image: ImageProxy) {
            val buffer = image.planes[0].buffer
            val data = buffer.toByteArray()
            val pixels = data.map {
                it.toInt() and 0xFF
            }

            val luma = pixels.average()
            listener(luma)
            image.close()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode== REQUEST_CODE_PERMISSIONS){
            if (allPermissionsGranted()){
                Toast.makeText(this,"Permissions not granted by the user.",Toast.LENGTH_SHORT).show()
                finish()
            }else{
//                val mDialog = MaterialDialog.Builder(this)
//                    .setTitle("Permissions Missing")
//                    .setMessage("Permissions not granted by the user.")
//                    .setCancelable(false)
//                    .setNegativeButton(
//                        "Cancel"
//                    ) { dialogInterface, which -> dialogInterface.dismiss() }
//                    .build()
//                mDialog.show()

                startCamera()
            }
        }
    }

    companion object{
        private const val TAG ="CameraXApp"
        private const val FILEMAN_FORMAT="yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS=10
        private val REQUIRED_PERMISIONS=
            mutableListOf(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            ).apply{
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }
}