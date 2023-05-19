package com.aurosaswat.quotesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    lateinit var mainViewModel: MainViewModel

    private val quoteText:TextView
    get() = findViewById(R.id.quoteText)

    private val quoteAuthor:TextView
    get() = findViewById(R.id.quoteAuthor)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /**
         *
         * There are two types of Context:-
         * i) Application Context
         * ii) Activity Context
         *
         *Note Activity can get destroyed due to screen rotation or any other kind of fuctionalities so passing Activity Context wouldn't be a good option
         * so Instead we pass Applicaion Context which will stay universal as its context will nevewr get destroyed as we are using the application ;)
         * */
    mainViewModel=ViewModelProvider(this,MainViewModelFactory(applicationContext)).get(MainViewModel::class.java)
    setQuote(mainViewModel.getQuote())
    }

    fun setQuote(quote: Quote){
        quoteText.text=quote.text
        quoteAuthor.text=quote.author
    }


    fun onPrevious(view: View){
        setQuote(mainViewModel.prevQuote())
    }
    fun onNext(view:View){
        setQuote(mainViewModel.nextQuote())
    }
    fun onShare(view:View){

        val intent=Intent(Intent.ACTION_SEND)
        intent.setType("text/plain")
        intent.putExtra(Intent.EXTRA_TEXT,mainViewModel.getQuote().text)
        startActivity(intent)
    }

}