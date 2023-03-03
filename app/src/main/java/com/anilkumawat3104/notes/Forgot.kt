package com.anilkumawat3104.notes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Telephony
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class Forgot : AppCompatActivity() {
    private lateinit var forgotEmail : EditText
    private lateinit var forgotB : RelativeLayout
    private lateinit var email : String
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot)
        forgotEmail = findViewById(R.id.forgotemail)
        firebaseAuth = FirebaseAuth.getInstance()
        forgotB = findViewById(R.id.fogotB)
        forgotB.setOnClickListener{
            email = forgotEmail.text.toString()
            if(email.isEmpty()){
                Toast.makeText(this,"Enter email",Toast.LENGTH_SHORT).show()
            }
            else{
               firebaseAuth.sendPasswordResetEmail(email)
                   .addOnSuccessListener {
                   Toast.makeText(this,"Email send to you for recover your password",Toast.LENGTH_SHORT)
                      finish()
                      val intent = Intent(this,MainActivity::class.java)
                      startActivity(intent)

               }
                   .addOnFailureListener{
                       Toast.makeText(this,"This account don't exist",Toast.LENGTH_SHORT)
                   }
            }
        }

    }
}