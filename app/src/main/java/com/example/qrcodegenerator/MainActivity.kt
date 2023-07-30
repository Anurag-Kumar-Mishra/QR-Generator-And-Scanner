package com.example.qrcodegenerator

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val generate: Button = findViewById(R.id.generate)
        val codeText: EditText = findViewById(R.id.codeText)
        val codeImage: ImageView = findViewById(R.id.codeImage)
        generate.setOnClickListener{

            val textInput = codeText.text.toString().trim()

            if(textInput.isNotEmpty()){
                val barcodeEncoder = BarcodeEncoder()
                val bitmap : Bitmap = barcodeEncoder.encodeBitmap(textInput, BarcodeFormat.QR_CODE, 400, 400)
                codeImage.setImageBitmap(bitmap)
            }
            else{
                Toast.makeText(this,"No text entered",Toast.LENGTH_SHORT).show()
            }
        }

        val scanButton: Button = findViewById(R.id.scanButton)
        scanButton.setOnClickListener {
            startActivity(Intent(this, ScanActivity::class.java))
        }

        val copyButton: ImageButton = findViewById(R.id.copyButton)
        copyButton.setOnClickListener {
            val clipboardText: EditText = findViewById(R.id.clipboardText)
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("clippedText",clipboardText.text)
            Toast.makeText(this, "Copied text to clipboard", Toast.LENGTH_LONG).show()
            clipboard.setPrimaryClip(clip)
        }
    }

    override fun onResume() {
        super.onResume()
        pasteClipboardText()
    }

    private fun pasteClipboardText(){
        val clipboardText : EditText = findViewById(R.id.clipboardText)
        clipboardText.setText("")
        val qrResult: Boolean = intent.getBooleanExtra("qrResult", false)
        if(qrResult) {
            clipboardText.post {
                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData: ClipData? = clipboard.primaryClip
                clipData.let {
                    clipboardText.setText(clipData?.getItemAt(0)?.text)
                }
            }
        }
    }
}