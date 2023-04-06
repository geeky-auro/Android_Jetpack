package com.aurosaswat.viewmodelapp

import androidx.lifecycle.ViewModel

//Donot try to keep any references towards the view as the chances to memory lekages gets increased

class MainViewModel: ViewModel() {

    var count:Int=0

    fun increment(){
        count++
//        setText()
    }
}