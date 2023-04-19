package com.aurosaswat.workingwithgallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog
//import kotlin.coroutines.jvm.internal.CompletedContinuation.context

class MainActivity : AppCompatActivity() {

    private lateinit var imgView:ImageView
    private lateinit var btnChange:Button

    private val contract=registerForActivityResult(ActivityResultContracts.GetContent()){
        imgView.setImageURI(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imgView=findViewById(R.id.imageView)
        btnChange=findViewById(R.id.button)
        val bottomSheetFragment=BottomSheetFragment()
        btnChange.setOnClickListener{


//            bottomSheetFragment.show(supportFragmentManager,"ButtonSheetDialog")

        //            contract.launch("image/*")
//            val mBottomSheetDialog = RoundedBottomSheetDialog(this)
//            val sheetView = layoutInflater.inflate(R.layout.dialog_2_my_rounded_bottom_sheet, null)
//            mBottomSheetDialog.setContentView(sheetView)
//            mBottomSheetDialog.show()
        }
    }
}