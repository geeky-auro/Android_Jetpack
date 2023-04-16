package com.aurosaswat.camerax

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import android.widget.Toast
import androidx.camera.lifecycle.ProcessCameraProvider
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.View.OnTouchListener
import androidx.camera.core.*
import androidx.camera.core.impl.PreviewConfig
import androidx.camera.extensions.internal.PreviewConfigProvider
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import com.aurosaswat.camerax.databinding.ActivityMainBinding
import com.google.common.util.concurrent.ListenableFuture
import java.io.File
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.Locale

typealias LumaListener = (luma: Double) -> Unit

class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

//        Check for Permisssions, if everything is granted then allow the camera to click the picture

        if (allPermissionsGranted()){
            startCamera()
        }else{
            ActivityCompat.requestPermissions(
                this,REQUIRED_PERMISIONS,REQUEST_CODE_PERMISSIONS
            )
        }

        // Set up the listeners for take photo and video capture buttons
        viewBinding.imageCaptureButton.setOnClickListener { takePhoto() }
//        viewBinding.videoCaptureButton.setOnClickListener { captureVideo() }

        cameraExecutor = Executors.newSingleThreadExecutor()
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

            val imageAnalzer=ImageAnalysis.Builder().build()
                .also {
                    it.setAnalyzer(cameraExecutor,LuminosityAnalyzer{
                        Log.d(TAG,"Average luminosity: $it")
                    })
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()
                // Bind use cases to camera
                val camera = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture,imageAnalzer)

                /**
                 * Enable Torch of the Camera ;_
                 *  Reference: https://proandroiddev.com/android-camerax-tap-to-focus-pinch-to-zoom-zoom-slider-eb88f3aa6fc6
                 *
                // Getting the CameraControl instance from the camera


                val enableTorchLF:ListenableFuture<Void> = cameraControl.enableTorch(true)
                enableTorchLF.addListener({
                try{
                enableTorchLF.get()
                // At this point, the torch has been successfully enabled
                }catch (exception:Exception){
                //  Handle any potential Error ;)
                }
                },cameraExecutor)
                /* Executor where the runnable callback code is run */
                 * */


                // Getting the CameraInfo instance from the camera
                val cameraInfo=camera.cameraInfo
                val cameraControl=camera.cameraControl
                viewBinding.viewFinder.setOnTouchListener(OnTouchListener{
                    view:View,motionEvent:MotionEvent ->
                    when(motionEvent.action){
                        MotionEvent.ACTION_DOWN-> return@OnTouchListener true
                        MotionEvent.ACTION_UP->{
                            val factory=viewBinding.viewFinder.meteringPointFactory

                            val point=factory.createPoint(motionEvent.x,motionEvent.y)

                            val action=FocusMeteringAction.Builder(point).build()

                            cameraControl.startFocusAndMetering(action)

                            return@OnTouchListener true

                        }
                        else -> return@OnTouchListener false
                    }
                })

                val listener=object :ScaleGestureDetector.SimpleOnScaleGestureListener(){
                    override fun onScale(detector: ScaleGestureDetector): Boolean {
                        // Get the camera's current zoom ratio
                        val currentZoomRatio=cameraInfo.zoomState.value?.zoomRatio?:0F

                        // Get the pinch gesture's scaling factor
                        val delta=detector.scaleFactor

                        // Update the camera's zoom ratio. This is an asynchronous operation that returns
                        // a ListenableFuture, allowing you to listen to when the operation completes.
                        cameraControl.setZoomRatio(currentZoomRatio*delta)

                        return true
                    }
                }

                val scaleGestureDetector=ScaleGestureDetector(this,listener)

//                Attach the pinch gesture listener to the viewFinder

                viewBinding.viewFinder.setOnTouchListener { v, event ->
                    scaleGestureDetector.onTouchEvent(event)
                    return@setOnTouchListener true
                }

                /**
                 *
                 * CameraInfo provides most of the above information wrapped in LiveData, this allows you to continually observe their changes.
                 * If you only need the current value of one of them, you can use LiveData.getValue().
                 *
                 * */

//                // Use the CameraInfo instance to observe the zoom state
////                cameraInfo.zoomState.observe(this, Observer {
////                    // Use the zoom state to retireve information about the zoom ratio, etc
////                    val currentZoomRatio=it.zoomRatio
////                })

            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))


    }

    private fun allPermissionsGranted() = REQUIRED_PERMISIONS.all {
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

                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults){
                    val msg = "Photo capture succeeded: ${outputFileResults.savedUri}"
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()

//                    val tsavedUri = outputFileResults.savedUri ?: Uri.fromFile(File(outputFileResults.savedFile!!.absolutePath))

                    val intent = Intent(this@MainActivity, CameraDisplayActivity::class.java)
                    intent.putExtra("URI_DATA",outputFileResults.savedUri.toString())
                    startActivity(intent)


                    Log.d(TAG, msg)
                }
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode== REQUEST_CODE_PERMISSIONS){
            if (allPermissionsGranted()){
                startCamera()
            }else{
                Toast.makeText(this,"Permissions not granted by the user.",Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }


    private class LuminosityAnalyzer(private val listener:LumaListener):ImageAnalysis.Analyzer{

        private fun ByteBuffer.toByteArray(): ByteArray {
            rewind()    // Rewind the buffer to zero
            val data = ByteArray(remaining())
            get(data)   // Copy the buffer into a byte array
            return data // Return the byte array
        }

        override fun analyze(image: ImageProxy) {
            val buffer=image.planes[0].buffer
            val data=buffer.toByteArray()
            val pixels=data.map{
                it.toInt() and 0xFF
            }

            val luma = pixels.average()
            listener(luma)

            image.close()
        }

    }



}