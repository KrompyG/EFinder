package com.example.test

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.coroutines.*
import khttp.get
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val CAMERA_REQUEST_CODE = 0
    private val GALLERY_REQUEST = 1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    // here will be code for sending photo to the server
    fun sendPhoto(view: View){
        val myToast = Toast.makeText(this, "halo", Toast.LENGTH_SHORT)
        GlobalScope.launch{
            get("https://www.google.com/")
            myToast.show()
        }

    }

    // giving control to camera
    fun openCamera(view: View){
        val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (callCameraIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(callCameraIntent, CAMERA_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    // showing taken photo
                    imageView.setImageBitmap(data.extras?.get("data") as Bitmap)

                }
            }
            GALLERY_REQUEST -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    // showing uploaded photo
                    imageView.setImageURI(data.data)
                }
            }
            else -> {
                Toast.makeText(this, "Unrecognized request code", Toast.LENGTH_LONG).show()
            }
        }
    }

    // giving control to gallery
    fun pickFromGallery(view: View) {
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"

        if (photoPickerIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(photoPickerIntent, GALLERY_REQUEST)
        }
    }
}