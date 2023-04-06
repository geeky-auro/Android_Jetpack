package com.aurosaswat.viewmodelapp

import androidx.lifecycle.ViewModel

//Donot try to keep any references towards the view as the chances to memory lekages gets increased

class MainViewModel(val initialvalue:Int): ViewModel() {

    var count:Int=initialvalue

    fun increment(){
        count++
//        setText()
    }
}