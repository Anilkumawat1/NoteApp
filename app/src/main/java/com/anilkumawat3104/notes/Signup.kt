package com.anilkumawat3104.notes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class Signup : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var enteredEmail : EditText
    private lateinit var enteredPassword : EditText
    private lateinit var signB : RelativeLayout
    private lateinit var email : String
    private lateinit var password : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        enteredEmail = findViewById(R.id.signemail)
        enteredPassword = findViewById(R.id.signpass)
        signB = findViewById(R.id.signB)
        firebaseAuth = FirebaseAuth.getInstance()

        signB.setOnClickListener{
            email = enteredEmail.text.toString()
            password = enteredPassword.text.toString()

            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Please enter Email and Password", Toast.LENGTH_SHORT).show()
            }
            else if(password.length<7){
                Toast.makeText(this, "Please enter 7 length password", Toast.LENGTH_SHORT).show()
            }
            else{
                firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this){
                    if(it.isSuccessful){
                        Toast.makeText(this,"Registration is succesfull",Toast.LENGTH_SHORT).show()
                        sendEmailverification()
                    }
                    else{
                        Toast.makeText(this,"Registration is failed",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


    }
    private fun sendEmailverification(){
        var firebaseUser  = firebaseAuth.currentUser
        firebaseUser!!.sendEmailVerification()
            .addOnSuccessListener {
                Toast.makeText(this, "Instructions Sent...", Toast.LENGTH_SHORT).show()
                firebaseAuth.signOut()
                finish()
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to send due to " + e.message, Toast.LENGTH_SHORT).show()
            }
        }




}