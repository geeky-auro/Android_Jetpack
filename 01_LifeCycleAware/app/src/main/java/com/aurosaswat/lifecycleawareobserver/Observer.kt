package com.aurosaswat.lifecycleawareobserver

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class Observer:LifecycleObserver {
//    It observes the lifecycle of our activity ;)
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate(){
        Log.d("MAIN","OBSERVER - ON CREATE")
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        Log.d("MAIN","OBSERVER - ON START")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
     fun onResume() {
        Log.d("MAIN","OBSERVER - ON RESUME")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
     fun onPause() {
        Log.d("MAIN","OBSERVER - ON PAUSE")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
     fun onStop() {
        Log.d("MAIN","OBSERVER - ON STOP")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
     fun onDestroy() {
        Log.d("MAIN","OBSERVER - ON DESTROY")
    }
}