package com.example.minipaint

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        val myCanvasView = MyCanvasView(this)
        myCanvasView.systemUiVisibility = SYSTEM_UI_FLAG_FULLSCREEN
        myCanvasView.contentDescription = getString(R.string.canvasContentDescription)
        setContentView(myCanvasView)
        // Create an AlertDialog.Builder instance
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose a color")
        // Add color options
        val colors = arrayOf("colorBackground", "colorPaint", "purple_200", "purple_500", "purple_700", "teal_200", "teal_700", "black", "white")
        builder.setItems(colors) { dialog, which ->
            // The 'which' argument contains the index position of the selected item
            myCanvasView.changeColor(colors[which])
        }
        // Create and show the AlertDialog
        val dialog = builder.create()
        dialog.show()

        // Create a new AlertDialog.Builder instance for background color
        val backgroundColorBuilder = AlertDialog.Builder(this)
        backgroundColorBuilder.setTitle("Choose a background color")

        backgroundColorBuilder.setItems(colors) { _, which ->
            // The 'which' argument contains the index position
            // of the selected item
            myCanvasView.changeBackgroundColor(colors[which])
        }

        backgroundColorBuilder.show()
    }
}