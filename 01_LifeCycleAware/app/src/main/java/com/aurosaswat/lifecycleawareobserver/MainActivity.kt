package com.aurosaswat.lifecycleawareobserver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lifecycle.addObserver(Observer())
        Log.d("MAIN","Activity onCreate")
    }

    override fun onStop() {
        super.onStop()
        lifecycle.addObserver(Observer())
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.addObserver(Observer())
    }

    override fun onStart() {
        super.onStart()
        lifecycle.addObserver(Observer())
    }

    override fun onPause() {
        super.onPause()
        lifecycle.addObserver(Observer())
    }


}