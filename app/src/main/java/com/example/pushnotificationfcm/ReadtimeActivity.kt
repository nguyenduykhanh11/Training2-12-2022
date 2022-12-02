package com.example.pushnotificationfcm

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.pushnotificationfcm.databinding.ActivityReadtimeBinding
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class ReadtimeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReadtimeBinding
    private val database: FirebaseDatabase by lazy { Firebase.database }
    private var id: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReadtimeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpListennerClickPush()
        setUpListenerClickDeleteValue()
        setUpEventRealtimeObject()
    }

    private fun setUpEventRealtimeObject() {
        binding.btnPushObject.setOnClickListener {
            pushObject()
            getDataObject()
        }
        binding.btnDeleteObject.setOnClickListener {
            deleteValue("user")
        }
    }

    private fun getDataObject() {
        val myRef = database.getReference("user")
        myRef.child(id.toString()).get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.value}")
            binding.tvResultObject.text = it.value.toString()
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }
    }

    private fun pushObject() {
        val myRef = database.getReference("user")
        val id = binding.edtInputId.text.toString()
        val name = binding.edtInputName.text.toString().trim()
        this.id = binding.edtInputId.text.toString().toInt()
        val user = user(id.toInt(), name)
        myRef.child(id).setValue(user, object :DatabaseReference.CompletionListener{
            override fun onComplete(error: DatabaseError?, ref: DatabaseReference) {
                Toast.makeText(this@ReadtimeActivity, "push success", Toast.LENGTH_SHORT).show()
            }
        })
        binding.edtInputId.text = null
        binding.edtInputName.text = null

    }


    private fun setUpListenerClickDeleteValue() {
        binding.btnDeleteData.setOnClickListener {
            deleteValue("test")
        }
    }

    private fun deleteValue(key : String) {
        val myRef = database.getReference(key)
        Log.d("this", id.toString())
        when(key){
            "user" -> {
                myRef.child(id.toString()).removeValue(object : DatabaseReference.CompletionListener{
                    override fun onComplete(error: DatabaseError?, ref: DatabaseReference) {
                        Toast.makeText(this@ReadtimeActivity, "delete success", Toast.LENGTH_SHORT).show()
                        binding.tvResultObject.text = ""
                        id = null
                    }
                })
            }
            "test" -> {
                myRef.removeValue(object : DatabaseReference.CompletionListener{
                    override fun onComplete(error: DatabaseError?, ref: DatabaseReference) {
                        Toast.makeText(this@ReadtimeActivity, "delete success", Toast.LENGTH_SHORT).show()
                        binding.tvResult.text = null
                    }
                })
            }
        }
    }


    private fun getData() {
        val myRef = database.getReference("test")
        myRef.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                binding.tvResult.text = dataSnapshot.getValue<String>()
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w("this", "Failed to read value.", error.toException())
            }
        })
    }

    private fun setUpListennerClickPush() {
        binding.btnPushData.setOnClickListener {
            pushData()
            getData()
        }
    }

    private fun pushData() {
        val myRef = database.getReference("test")
        myRef.setValue(binding.edtInput.text.toString(), object : DatabaseReference.CompletionListener{
            override fun onComplete(error: DatabaseError?, ref: DatabaseReference) {
                Toast.makeText(this@ReadtimeActivity, "push success", Toast.LENGTH_SHORT).show()
            }
        })
        binding.edtInput.text = null
    }
}