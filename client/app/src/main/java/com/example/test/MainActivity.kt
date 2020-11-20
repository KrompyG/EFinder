package com.example.test

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import khttp.get
import khttp.post
import khttp.structures.files.FileLike
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity() {
    private val CAMERA_REQUEST_CODE = 0
    private val GALLERY_REQUEST = 1
    var BYTE_ARRAY_PHOTO = ByteArrayOutputStream()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    // here will be code for sending photo to the server
    fun sendPhoto(view: View){
        val myToast = Toast.makeText(this, "no content", Toast.LENGTH_SHORT)
        if(BYTE_ARRAY_PHOTO.size() == 0){
            myToast.show()
            return
        }

        GlobalScope.launch(Dispatchers.Main){
            val postOperation = async(Dispatchers.IO){
                //get("http://192.168.43.244:5000/")
                post(
                    url="http://192.168.43.244:5000/upload",
                    files= listOf(
                        FileLike("imagefile","photo.jpeg", BYTE_ARRAY_PHOTO.toByteArray())
                    )
                )
            }

            val response = postOperation.await()

            myToast.setText(response.text)
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
                    val bitmap_photo = data.extras?.get("data") as Bitmap
                    bitmap_photo.compress(Bitmap.CompressFormat.JPEG, 100, BYTE_ARRAY_PHOTO)

                    imageView.setImageBitmap(bitmap_photo)
                }
            }
            GALLERY_REQUEST -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    // showing uploaded photo
                    val bitmap_photo = data.extras?.get("data") as Bitmap
                    bitmap_photo.compress(Bitmap.CompressFormat.JPEG, 100, BYTE_ARRAY_PHOTO)

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