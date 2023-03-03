package com.anilkumawat3104.notes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var fogot : TextView
    private lateinit var sign : RelativeLayout
    private lateinit var loginB : RelativeLayout
    private lateinit var email : String
    private lateinit var password : String
    private lateinit var loginemail : EditText
    private lateinit var loginpass : EditText
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()
        fogot = findViewById(R.id.fogot)
        sign = findViewById(R.id.sign)
        loginB = findViewById(R.id.loginB)
        loginemail = findViewById(R.id.loginemail)
        loginpass = findViewById(R.id.loginpass)
        firebaseAuth = FirebaseAuth.getInstance()
        var firebaseUser = firebaseAuth.currentUser

        if(firebaseUser!=null){
            finish()
            val intent = Intent(this,notes::class.java)
            startActivity(intent)
        }
        fogot.setOnClickListener {
            val intent = Intent(this, Forgot::class.java)
            startActivity(intent)
        }
        sign.setOnClickListener{
            val intent = Intent(this,Signup::class.java)
            startActivity(intent)
        }
        loginB.setOnClickListener{
            email = loginemail.text.toString()
            password = loginpass.text.toString()

            if(email.isEmpty()||password.isEmpty()){
                Toast.makeText(this,"Please enter the email and password",Toast.LENGTH_SHORT)
            }
            else{
                firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this){
                    if(it.isSuccessful){
                       accountVerification()

                    }
                    else{
                        Toast.makeText(this,"Account does not exist",Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
              }
    private fun accountVerification(){
       var firebaseUser = firebaseAuth.currentUser
        if(firebaseUser!!.isEmailVerified==true){
            Toast.makeText(this,"Logged In",Toast.LENGTH_SHORT)
            finish()
            val intent = Intent(this,notes::class.java)
            startActivity(intent)
        }
        else{
            Toast.makeText(this,"Verify your email",Toast.LENGTH_SHORT)
            firebaseAuth.signOut()
        }
    }
}