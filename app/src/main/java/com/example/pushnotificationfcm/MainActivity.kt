package com.example.pushnotificationfcm

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.icu.util.Calendar
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pushnotificationfcm.databinding.ActivityMainBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream

const val REQUEST_IMAGE_CAPTURE = 1

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val mAdapter: Adapter by lazy { Adapter() }
    private  val storage: FirebaseStorage by lazy {FirebaseStorage.getInstance() }
    private lateinit var storageRef: StorageReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setUplistenerCamera()
        setUpListenerClickSave()
        setUpViewAdapter()
        getDataFromStorage()
        setUpListenerClickToRealtime()
    }

    private fun setUpListenerClickToRealtime() {
        binding.btnToReadTime.setOnClickListener {
            val intent = Intent(this, ReadtimeActivity::class.java)
            startActivity(intent)
        }
    }

    @SuppressLint("NewApi")
    private fun getDataFromStorage() {
//
    }

    private fun setUpViewAdapter() {
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 2,GridLayoutManager.VERTICAL, false)
            adapter = mAdapter
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null){
            val bitmap = data.extras?.get("data") as Bitmap
            binding.image.setImageBitmap(bitmap)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    @SuppressLint("NewApi")
    private fun setUpListenerClickSave() {
        binding.btnSaveFireBase.setOnClickListener {
            storageRef = storage.reference
            val calendar = Calendar.getInstance()
            val mountainsRef = storageRef.child("image${calendar.timeInMillis}.png")
            binding.image.isDrawingCacheEnabled = true
            binding.image.buildDrawingCache()
            val bitmap = (binding.image.drawable as BitmapDrawable).bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val data = baos.toByteArray()
            val uploadTask = mountainsRef.putBytes(data)
            uploadTask.addOnFailureListener {
                Toast.makeText(this, "thất bại", Toast.LENGTH_SHORT).show()

            }.addOnSuccessListener { taskSnapshot ->
                Toast.makeText(this, "Thành công", Toast.LENGTH_SHORT).show()
                Log.d("this", taskSnapshot.uploadSessionUri.toString())
            }
        }
    }

    private fun setUplistenerCamera() {
        binding.btnMayAnh.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
        }
    }


}