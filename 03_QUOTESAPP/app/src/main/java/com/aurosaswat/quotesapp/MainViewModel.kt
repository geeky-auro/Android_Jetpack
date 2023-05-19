package com.aurosaswat.quotesapp

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.gson.Gson

/**
 *
 * This Java Class contains all the the data required to the UI
 *
 * */

class MainViewModel(val context:Context) : ViewModel(){
    private var quotelist :Array<Quote>
    private var index = 0

    init {
        quotelist=loadQuoteFromAssets()
    }

    private fun loadQuoteFromAssets(): Array<Quote> {
        /**
         *
         * To Read the JSON File we need Activity Context which we can only get from MainActivity
         * But we need to use it here inorder to do so we ViewModel Factory to pass data from Main Activity to ViewModel Class
         *
         *
         * */

        // Open the QUotes.json file
        val inputStream=context.assets.open("quotes.json")
        val size=inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()

        /**
         * Steps Followed above:-
         * ST1:Open the file
         * ST2:Find the size of the File (returns an Integer)
         * ST3:Declare a ByteArray "Named as Buffer" and pass the size of the "quotes.json"
         * ST4:Read the Input Stream
         * ST5:Close the InputStream
         * */

//        Convert the data in the Byte Array into String ;)
        val json=String(buffer,Charsets.UTF_8)
        val gson=Gson()
        return gson.fromJson(json, Array<Quote>::class.java)
    }

    fun getQuote()=quotelist[index]
    fun nextQuote()=quotelist[(++index)%quotelist.size-1]
    fun prevQuote()=quotelist[(--index)%quotelist.size-1]

}