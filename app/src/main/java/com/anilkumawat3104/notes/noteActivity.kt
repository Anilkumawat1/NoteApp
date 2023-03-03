package com.anilkumawat3104.notes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class noteActivity : AppCompatActivity() {
    private lateinit var titlenote : EditText
    private lateinit var notetext : EditText
    private lateinit var save : FloatingActionButton
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var firebasefirestore : FirebaseFirestore
    private lateinit var documentReference: DocumentReference
    private lateinit var title : String
    private lateinit var note : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        titlenote = findViewById(R.id.newNotetitle)
        notetext = findViewById(R.id.newNotetext)
        save = findViewById(R.id.savenote)

        firebaseAuth = FirebaseAuth.getInstance()
        firebasefirestore = FirebaseFirestore.getInstance()
        firebaseUser = firebaseAuth.currentUser!!

        save.setOnClickListener{
            title = titlenote.text.toString()
            note = notetext.text.toString()
            if(title.isEmpty()||note.isEmpty()) {
                Toast.makeText(this, "Enter note and title", Toast.LENGTH_SHORT)
            }
            else{
                documentReference = firebasefirestore.collection("notes").document(firebaseUser.uid).collection("myNotes").document()
                val mutableMap = mutableMapOf("title" to title, "content" to note)
                documentReference.set(mutableMap).addOnSuccessListener {
                      Toast.makeText(this,"add to data base",Toast.LENGTH_SHORT)
                    finish()
                    val intent = Intent(this,notes::class.java)
                    startActivity(intent)
                }
                    .addOnFailureListener{
                        Toast.makeText(this,"add to data base",Toast.LENGTH_SHORT)
                    }
            }
        }
    }
}