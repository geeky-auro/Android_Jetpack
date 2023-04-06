package com.aurosaswat.viewmodelapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {


    lateinit var txtCounter:TextView
    lateinit var mainViewModel:MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        Parameterized View Model ;)
        mainViewModel=ViewModelProvider(this,MainViewModelFactory(10)).get(MainViewModel::class.java)
        txtCounter=findViewById(R.id.txtCounter)
        setText()
    }

    /**
     * Views will show Data
     * View Model will hold Data
     * */

    fun increment(v:View){
      mainViewModel.increment()
      setText()
    }
    fun setText() {
        txtCounter.text=mainViewModel.count.toString()
    }
}