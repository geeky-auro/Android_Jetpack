package com.aurosaswat.bottomsheetdialog


import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog

lateinit var btnShowBottomSheet: Button
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnShowBottomSheet = findViewById(R.id.idBtnShowBottomSheet)
        btnShowBottomSheet.setOnClickListener {
            val dialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog, null)
            val btnClose = view.findViewById<Button>(R.id.idBtnDismiss)
            btnClose.setOnClickListener{
                dialog.dismiss()
            }
            dialog.setCancelable(false)

            dialog.setContentView(view)

            dialog.show()
        }


    }
}